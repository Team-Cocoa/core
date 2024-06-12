package kr.teamcocoa.core.bungee.main;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Command;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import kr.teamcocoa.core.bungee.commands.DiscordCommand;
import kr.teamcocoa.core.bungee.commands.RuleCommand;
import kr.teamcocoa.core.bungee.commands.StoreCommand;
import kr.teamcocoa.core.bungee.commands.TwitterCommand;
import kr.teamcocoa.core.bungee.listener.*;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.mongo.MongoDB;
import kr.teamcocoa.core.mysql.MySQLManager;
import kr.teamcocoa.core.mysql.pool.ConnectionPoolManager;
import kr.teamcocoa.core.utils.AsyncDetector;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;


@Singleton
@PlatformPlugin(
        platform = "bungeecord",
        name = "TeamCocoa-Core",
        version = "1.0",
        authors = "fixca",
        commands = {
                @Command(name = "discord"),
                @Command(name = "rule"),
                @Command(name = "store"),
                @Command(name = "twitter")
        },
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
        AsyncDetector.setMainThread(Thread.currentThread());

        loadDatabases();

        instance.getProxy().registerChannel(StringConfig.MESSAGE_CHANNEL);
        pluginManager.registerListener(instance, new PluginMessageListener());
        pluginManager.registerListener(instance, new ChatListener());

        pluginManager.registerCommand(instance, new DiscordCommand("discord"));
        pluginManager.registerCommand(instance, new RuleCommand("rules"));
        pluginManager.registerCommand(instance, new StoreCommand("store"));
        pluginManager.registerCommand(instance, new TwitterCommand("twitter"));
    }

    @Override
    public void onDisable() {
        instance.getProxy().unregisterChannel(StringConfig.MESSAGE_CHANNEL);

        disconnectDatabases();
    }

    private void loadDatabases() {
        MySQLManager.startKeepAlive();
        ConnectionPoolManager.startKeepAlive();

        MongoDB.connectDatabase();
    }

    private void disconnectDatabases() {
        MySQLManager.stopKeepAlive();
        MySQLManager.disconnectAllConnections();
        ConnectionPoolManager.stopKeepAlive();
        ConnectionPoolManager.disconnectAllConnections();

        MongoDB.disconnectDatabase();
    }
}
