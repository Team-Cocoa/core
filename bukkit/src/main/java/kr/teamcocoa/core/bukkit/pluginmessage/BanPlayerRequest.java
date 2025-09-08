package kr.teamcocoa.core.bukkit.pluginmessage;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class BanPlayerRequest {

    public static JsonObject sendBanPlayer(Player player, String reason) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ban_player");
        jsonObject.addProperty("player", player.getName());
        jsonObject.addProperty("reason", reason);
        return jsonObject;
    }

}
