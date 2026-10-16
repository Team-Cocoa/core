package kr.teamcocoa.core.bukkit.main;


import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import eu.cloudnetservice.wrapper.configuration.WrapperConfiguration;
import kr.teamcocoa.core.bukkit.listeners.AntiCheatListener;
import kr.teamcocoa.core.cloudnet.listeners.CloudNetEventManager;
import kr.teamcocoa.core.cloudnet.utils.CloudNetServiceUtils;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.utils.AsyncDetector;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
    }

    private void loadListeners() {
        pluginManager.registerEvents(new AntiCheatListener(), instance);

        cloudnetEventManager.registerListener(new CloudNetEventManager());
    }

    public double[] getTPS() {
        return MinecraftServer.getServer().recentTps;
    }
}
