package kr.teamcocoa.core.bukkit.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketUtils {

    public static void sendTitle(
            Player player,
            String title,
            String subtitle,
            int fadeIn,
            int stay,
            int fadeOut) {
        WrapperPlayServerClearTitles titleResetPacket = new WrapperPlayServerClearTitles(true);
        WrapperPlayServerSetTitleTimes setTimePacket = new WrapperPlayServerSetTitleTimes(fadeIn, stay, fadeOut);
        WrapperPlayServerSetTitleText setTitlePacket = new WrapperPlayServerSetTitleText(
                AdventureSerializer.serializer().fromJson("{\"text\": \"" + StringUtils.color(title) + "\"}"));
        WrapperPlayServerSetTitleSubtitle setSubTitlePacket = new WrapperPlayServerSetTitleSubtitle(
                AdventureSerializer.serializer().fromJson("{\"text\": \"" + StringUtils.color(subtitle) + "\"}"));

        sendPackets(player, titleResetPacket, setTimePacket, setTitlePacket, setSubTitlePacket);
    }

    public static void sendBar(Player player, String message) {
        Component chatComponent = AdventureSerializer.serializer().fromJson("{\"text\": \"" + StringUtils.color(message) + "\"}");
        ChatType chatType = ChatTypes.GAME_INFO;
        ChatMessage chatMessage = new ChatMessageLegacy(chatComponent, chatType);

        WrapperPlayServerChatMessage packet = new WrapperPlayServerChatMessage(chatMessage);

        sendPackets(player, packet);
    }

    public static void sendPackets(Player player, PacketWrapper<?>... packets) {
        try {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packets);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
