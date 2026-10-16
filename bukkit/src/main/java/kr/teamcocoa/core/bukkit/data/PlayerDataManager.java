package kr.teamcocoa.core.bukkit.data;

import kr.teamcocoa.core.bukkit.task.PluginExecutors;
import kr.teamcocoa.core.bukkit.task.SafeTask;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Loads every registered source in parallel when a player joins, and saves every
 * {@link PlayerDataStore} on quit, periodically and on shutdown.
 *
 * <p>Call {@link #start()} from onEnable after registering the sources. {@link #shutdown()} runs
 * automatically when the plugin is disabled; call it yourself from onDisable if the rest of
 * onDisable must run after the final save.
 */
public abstract class PlayerDataManager<P> implements Listener {

    @Getter
    private final Plugin plugin;

    private final String name;

    private final List<PlayerDataSource<?>> sources = new ArrayList<>();

    private final Map<UUID, Entry<P>> entries = new ConcurrentHashMap<>();

    // Main thread only. Identifies the join a load belongs to, so a load that finishes after the
    // player quit (or quit and rejoined) is dropped
    private final Map<UUID, Object> sessions = new HashMap<>();

    // A rejoin waits for the quit save, so it never loads data older than what is being saved
    private final Map<UUID, CompletableFuture<Void>> pendingSaves = new ConcurrentHashMap<>();

    private final ExecutorService loadPool;

    private final ExecutorService savePool;

    private final Executor mainThread;

    private ScheduledExecutorService autoSave;

    private boolean started;

    private volatile boolean shutdown;

    protected PlayerDataManager(Plugin plugin, String name, int threads) {
        this.plugin = plugin;
        this.name = name;
        this.loadPool = PluginExecutors.create(plugin, name + "-load", threads);
        this.savePool = PluginExecutors.create(plugin, name + "-save", threads);
        this.mainThread = PluginExecutors.mainThread(plugin);
        PluginExecutors.addShutdownHook(plugin, this::shutdown);
    }

    protected abstract P create(Player player, LoadedData data);

    /**
     * Main thread, right after the player joins and before anything is loaded.
     */
    protected void onLoading(Player player) {
    }

    /**
     * Main thread, once every source is loaded and the player is still online.
     */
    protected abstract void onLoaded(Player player, P value);

    /**
     * Main thread, on quit before the data is saved. The player is no longer returned by
     * {@link #get(Player)}.
     */
    protected void onUnload(Player player, P value) {
    }

    protected String getDefaultKickMessage() {
        return "&cFailed to load your data. Please contact a developer.";
    }

    public <T> void register(PlayerDataSource<T> source) {
        if (started) {
            throw new IllegalStateException("Sources must be registered before start()");
        }
        sources.add(source);
    }

    public void start() {
        if (started) {
            return;
        }
        started = true;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            load(player);
        }
    }

    public void startAutoSave(long period, TimeUnit unit) {
        if (autoSave != null) {
            throw new IllegalStateException("Auto save is already running");
        }
        autoSave = PluginExecutors.createScheduled(plugin, name + "-autosave");
        autoSave.scheduleWithFixedDelay(SafeTask.wrap(plugin, name + "-autosave", this::saveAll), period, period, unit);
    }

    public P get(Player player) {
        return player == null ? null : get(player.getUniqueId());
    }

    public P get(UUID uuid) {
        Entry<P> entry = entries.get(uuid);
        return entry == null ? null : entry.value;
    }

    public Collection<P> getAll() {
        List<P> values = new ArrayList<>(entries.size());
        for (Entry<P> entry : entries.values()) {
            values.add(entry.value);
        }
        return values;
    }

    public void saveAll() {
        for (Entry<P> entry : entries.values()) {
            save(entry);
        }
    }

    public synchronized void shutdown() {
        if (shutdown) {
            return;
        }
        shutdown = true;
        HandlerList.unregisterAll(this);

        if (autoSave != null) {
            autoSave.shutdown();
            PluginExecutors.awaitTermination(plugin, name + "-autosave", autoSave);
        }
        loadPool.shutdownNow();
        savePool.shutdown();
        PluginExecutors.awaitTermination(plugin, name + "-save", savePool);

        saveAll();
        entries.clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        load(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        sessions.remove(uuid);

        Entry<P> entry = entries.remove(uuid);
        if (entry == null) {
            return;
        }

        try {
            onUnload(player, entry.value);
        } catch (Throwable throwable) {
            plugin.getLogger().log(Level.SEVERE, "Failed to unload " + player.getName(), throwable);
        }

        CompletableFuture<Void> save = CompletableFuture.runAsync(() -> save(entry), savePool);
        pendingSaves.put(uuid, save);
        save.whenComplete((ignored, error) -> pendingSaves.remove(uuid, save));
    }

    private void load(Player player) {
        UUID uuid = player.getUniqueId();
        Object session = new Object();
        sessions.put(uuid, session);

        onLoading(player);

        CompletableFuture<Void> pendingSave = pendingSaves.getOrDefault(uuid, CompletableFuture.completedFuture(null));

        Map<PlayerDataSource<?>, CompletableFuture<?>> futures = new LinkedHashMap<>();
        for (PlayerDataSource<?> source : sources) {
            futures.put(source, pendingSave.thenApplyAsync(ignored -> loadSource(source, player), loadPool));
        }

        CompletableFuture.allOf(futures.values().toArray(new CompletableFuture<?>[0]))
                .thenRunAsync(() -> {
                    if (shutdown || sessions.get(uuid) != session || !player.isOnline()) {
                        return;
                    }
                    Map<PlayerDataSource<?>, Object> values = new HashMap<>();
                    futures.forEach((source, future) -> values.put(source, future.join()));
                    LoadedData data = new LoadedData(values);

                    P value = create(player, data);
                    entries.put(uuid, new Entry<>(value, data));
                    onLoaded(player, value);
                }, mainThread)
                .exceptionally(error -> {
                    failLoad(player, session, error);
                    return null;
                });
    }

    private Object loadSource(PlayerDataSource<?> source, Player player) {
        try {
            return source.load(player);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private void failLoad(Player player, Object session, Throwable error) {
        if (shutdown) {
            return;
        }
        Throwable cause = error;
        while (cause instanceof CompletionException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        plugin.getLogger().log(Level.SEVERE, "Failed to load " + player.getName() + "'s data", cause);

        String kickMessage = StringUtils.color(cause instanceof PlayerDataLoadException
                ? cause.getMessage()
                : getDefaultKickMessage());
        mainThread.execute(() -> {
            if (sessions.get(player.getUniqueId()) == session && player.isOnline()) {
                player.kickPlayer(kickMessage);
            }
        });
    }

    private void save(Entry<P> entry) {
        synchronized (entry) {
            for (PlayerDataSource<?> source : sources) {
                if (source instanceof PlayerDataStore<?> store) {
                    saveStore(store, entry.data.get(source));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Versioned> void saveStore(PlayerDataStore<T> store, Object value) {
        T data = (T) value;
        if (data == null || !data.isEdited()) {
            return;
        }
        long revision = data.getRevision();
        try {
            if (store.save(data)) {
                data.markSaved(revision);
            }
        } catch (Throwable throwable) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save " + name + " data", throwable);
        }
    }

    private record Entry<P>(P value, LoadedData data) {
    }

}
