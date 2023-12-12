package kr.teamcocoa.core.bungee.handler;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;

public class ServerMoveHandler {

    public static void handle(JsonObject jsonObject) {
        Logger logger = ProxyServer.getInstance().getLogger();

        UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        if (player == null) {
            logger.info(
                    "ServerMoveHandler failed because player is null. UUID : " + uuid.toString());
            return;
        }

        String serverName = jsonObject.get("server").getAsString();
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);

        if (serverInfo == null) {
            logger.info(
                    "ServerMoveHandler failed because serverInfo is null. serverInfo : " + serverName);
            return;
        }

        player.connect(serverInfo);
        logger.info(MessageFormat.format(
                "Successfully moved {0} to {1}.", uuid.toString(), serverName));

    }

}
