package kr.teamcocoa.core.bukkit.pluginmessage;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class ServiceMoveRequest {

    public static JsonObject sendServerMoveRequest(Player player, String serverName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "server_move");
        jsonObject.addProperty("uuid", player.getUniqueId().toString());
        jsonObject.addProperty("server", serverName);
        return jsonObject;
    }



}