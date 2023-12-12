package kr.teamcocoa.core.bukkit.utils;

import com.google.gson.JsonObject;
import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import kr.teamcocoa.core.config.StringConfig;
import kr.teamcocoa.core.utils.JsonUtils;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BukkitPluginMessageUtils {

    public static void sendViaPlayer(Player player, JsonObject jsonObject) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(stream);
            output.writeUTF(JsonUtils.stringify(jsonObject));
            player.sendPluginMessage(BukkitCoreBootstrap.getInstance(), StringConfig.MESSAGE_CHANNEL, stream.toByteArray());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
