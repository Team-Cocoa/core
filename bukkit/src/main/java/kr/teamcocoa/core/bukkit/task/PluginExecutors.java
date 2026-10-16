package kr.teamcocoa.core.bukkit.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * Executors owned by a plugin. They are shut down and awaited when the plugin is disabled,
 * after the plugin's shutdown hooks have run.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginExecutors {

    private static final long SHUTDOWN_TIMEOUT_SECONDS = 30;

    // Work submitted after shutdown (for example a save from onDisable) runs on the caller
    // instead of being rejected or silently dropped
    private static final RejectedExecutionHandler RUN_ON_CALLER = (task, executor) -> task.run();

    private static final Map<String, Owned> OWNED = new ConcurrentHashMap<>();

    public static ExecutorService create(Plugin plugin, String name, int threads) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory(plugin, name), RUN_ON_CALLER);
        executor.allowCoreThreadTimeOut(true);
        owned(plugin).executors.add(new NamedExecutor(name, executor));
        return executor;
    }

    public static ScheduledExecutorService createScheduled(Plugin plugin, String name) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory(plugin, name));
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        owned(plugin).executors.add(new NamedExecutor(name, executor));
        return executor;
    }

    public static Executor mainThread(Plugin plugin) {
        return task -> {
            if (Bukkit.isPrimaryThread()) {
                task.run();
                return;
            }
            Bukkit.getScheduler().runTask(plugin, task);
        };
    }

    public static void addShutdownHook(Plugin plugin, Runnable hook) {
        owned(plugin).hooks.add(hook);
    }

    public static void shutdown(Plugin plugin) {
        Owned owned = OWNED.remove(plugin.getName());
        if (owned == null) {
            return;
        }

        for (Runnable hook : owned.hooks) {
            try {
                hook.run();
            } catch (Throwable throwable) {
                plugin.getLogger().log(Level.SEVERE, "Shutdown hook failed", throwable);
            }
        }

        for (NamedExecutor named : owned.executors) {
            named.executor.shutdown();
        }
        for (NamedExecutor named : owned.executors) {
            awaitTermination(plugin, named.name, named.executor);
        }
    }

    public static void awaitTermination(Plugin plugin, String name, ExecutorService executor) {
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                plugin.getLogger().warning("Executor '" + name + "' did not finish within "
                        + SHUTDOWN_TIMEOUT_SECONDS + " seconds; interrupting it");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static Owned owned(Plugin plugin) {
        return OWNED.computeIfAbsent(plugin.getName(), key -> new Owned());
    }

    private static ThreadFactory threadFactory(Plugin plugin, String name) {
        AtomicInteger counter = new AtomicInteger();
        return runnable -> {
            Thread thread = new Thread(runnable, plugin.getName() + "-" + name + "-" + counter.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, throwable) ->
                    plugin.getLogger().log(Level.SEVERE, "Uncaught exception in " + t.getName(), throwable));
            return thread;
        };
    }

    private static class Owned {
        private final List<Runnable> hooks = new CopyOnWriteArrayList<>();
        private final List<NamedExecutor> executors = new CopyOnWriteArrayList<>();
    }

    private record NamedExecutor(String name, ExecutorService executor) {
    }

}
