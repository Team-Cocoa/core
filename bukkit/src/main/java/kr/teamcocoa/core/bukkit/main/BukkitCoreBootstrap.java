package kr.teamcocoa.core.bukkit.main;


import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import kr.teamcocoa.core.bukkit.listeners.AntiCheatListener;
import kr.teamcocoa.core.bukkit.utils.BukkitPluginMessageUtils;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.utils.AsyncDetector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
@PlatformPlugin(
        platform = "bukkit",
        name = "TeamCocoa-Core",
        version = "1.0",
        authors = "fixca",
        pluginFileNames = "plugin.yml",
        api = "1.13"
)
public class BukkitCoreBootstrap implements PlatformEntrypoint {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static PermissionManagement permissionManagement;

    private PluginManager pluginManager;


    @Inject
    BukkitCoreBootstrap(
            @NonNull JavaPlugin plugin,
            @NonNull PluginManager pluginManager,
            @NonNull PermissionManagement permissionManagement) {
        BukkitCoreBootstrap.instance = plugin;
        BukkitCoreBootstrap.permissionManagement = permissionManagement;
        this.pluginManager = pluginManager;
    }

    @Override
    public void onLoad() {
        AsyncDetector.setMainThread(Thread.currentThread());
        loadListeners();

        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), StringConfig.MESSAGE_CHANNEL);
    }

    @Override
    public void onDisable() {
        getInstance().getServer().getMessenger().unregisterOutgoingPluginChannel(getInstance(), StringConfig.MESSAGE_CHANNEL);
    }

    private void loadListeners() {
        pluginManager.registerEvents(new AntiCheatListener(), instance);
    }
}
