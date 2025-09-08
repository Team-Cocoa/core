package kr.teamcocoa.core.bungee.listener;

import com.google.gson.JsonObject;
import kr.teamcocoa.core.bungee.handler.ServerMoveHandler;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.utils.JsonUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.text.MessageFormat;
import java.util.logging.Logger;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        if(!e.getTag().equalsIgnoreCase(StringConfig.MESSAGE_CHANNEL)) {
            return;
        }

        Logger logger = ProxyServer.getInstance().getLogger();

        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
            DataInputStream input = new DataInputStream(stream);
            String rawData = input.readUTF();

            if(e.getReceiver() instanceof ProxiedPlayer player) {
                logger.info(MessageFormat.format(
                        "Player {0} sent plugin message to core. {1}",
                        player.getName(), rawData));
            }

            if(e.getReceiver() instanceof Server server) {
                logger.info(MessageFormat.format(
                        "Server {0} sent plugin message to core. {1}",
                        server.getInfo().getName(), rawData));
            }

            JsonObject object = JsonUtils.parse(rawData);

            String type = object.get("type").getAsString();

            switch (type) {
                case "server_move" -> {
                    ServerMoveHandler.handle(object);
                }
                case "ban_player" -> {

                    String playerName = object.get("player").getAsString();
                    String reason = object.get("reason").getAsString();

                    ProxyServer.getInstance().getPluginManager().dispatchCommand(
                            ProxyServer.getInstance().getConsole(),
                        MessageFormat.format(
                                "tempipban {0} 30d {1}", playerName, reason));
                }
            }

        }
        catch (Exception e1) {
            e1.printStackTrace();
        }


    }


}