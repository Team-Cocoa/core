package kr.teamcocoa.core.bukkit.main;


import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import eu.cloudnetservice.wrapper.configuration.WrapperConfiguration;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import kr.teamcocoa.core.bukkit.listeners.AntiCheatListener;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapterProvider;
import kr.teamcocoa.core.cloudnet.listeners.CloudNetEventManager;
import kr.teamcocoa.core.cloudnet.utils.CloudNetServiceUtils;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.utils.AsyncDetector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ServiceLoader;

@Singleton
@PlatformPlugin(
        platform = "bukkit",
        name = "TeamCocoa-Core",
        version = "1.0",
        authors = "fixca",
        pluginFileNames = "plugin.yml",
        api = "1.13",
        dependencies = {
                @Dependency(name = "spark")
        }
)
public class BukkitCoreBootstrap implements PlatformEntrypoint {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static PermissionManagement permissionManagement;

    private PluginManager pluginManager;

    @Getter
    private static EventManager cloudnetEventManager;

    @Getter
    private static WrapperConfiguration wrapperConfiguration;

    @Getter
    private static BukkitVersionAdapter versionAdapter;

    @Inject
    BukkitCoreBootstrap(
            @NonNull JavaPlugin plugin,
            @NonNull PluginManager pluginManager,
            @NonNull PermissionManagement permissionManagement,
            @NonNull EventManager cloudnetEventManager,
            @NonNull WrapperConfiguration wrapperConfiguration) {
        BukkitCoreBootstrap.instance = plugin;
        BukkitCoreBootstrap.permissionManagement = permissionManagement;
        BukkitCoreBootstrap.cloudnetEventManager = cloudnetEventManager;
        BukkitCoreBootstrap.wrapperConfiguration = wrapperConfiguration;
        this.pluginManager = pluginManager;
    }

    @Override
    public void onLoad() {
        AsyncDetector.setMainThread(Thread.currentThread());

        CloudNetServiceUtils.setInitServiceName(wrapperConfiguration);

        loadPacketEvents();
        loadVersionAdapter();
        loadListeners();

        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), StringConfig.MESSAGE_CHANNEL);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            for(String command : new String[] {
                    "spark tickmonitor",
                    "spark profiler start"}) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }, 100L);

    }

    @Override
    public void onDisable() {
        getInstance().getServer().getMessenger().unregisterOutgoingPluginChannel(getInstance(), StringConfig.MESSAGE_CHANNEL);
        PacketEvents.getAPI().terminate();
    }

    private void loadPacketEvents() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(instance));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();
    }

    private void loadVersionAdapter() {
        ServerVersion serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        for (BukkitVersionAdapterProvider provider : ServiceLoader.load(BukkitVersionAdapterProvider.class, getClass().getClassLoader())) {
            if (provider.isSupported(serverVersion)) {
                versionAdapter = provider.create();
                return;
            }
        }
        throw new IllegalStateException("Unsupported server version: " + serverVersion.getReleaseName());
    }

    private void loadListeners() {
        pluginManager.registerEvents(new AntiCheatListener(), instance);

        cloudnetEventManager.registerListener(new CloudNetEventManager());
    }

    public double[] getTPS() {
        return versionAdapter.getTPS();
    }
}
