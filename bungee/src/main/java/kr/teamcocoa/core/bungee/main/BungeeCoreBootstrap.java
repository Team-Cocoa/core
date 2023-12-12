package kr.teamcocoa.core.bungee.main;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import kr.teamcocoa.core.bungee.listener.PluginMessageListener;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;


@Singleton
@PlatformPlugin(
        platform = "bungee",
        name = "TeamCocoa-Core",
        version = "1.0",
        authors = "fixca",
        pluginFileNames = "bungee.yml"
)
public class BungeeCoreBootstrap implements PlatformEntrypoint {

    @Getter
    private static Plugin instance;

    @Getter
    private static PluginManager pluginManager;

    @Inject
    public BungeeCoreBootstrap(
            @NonNull Plugin plugin,
            @NonNull PluginManager pluginManager
    ) {
        BungeeCoreBootstrap.instance = plugin;
        BungeeCoreBootstrap.pluginManager = pluginManager;
    }

    @Override
    public void onLoad() {
        pluginManager.registerListener(instance, new PluginMessageListener());
    }
}
