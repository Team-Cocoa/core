package kr.teamcocoa.core.bukkit.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerActionBar;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle.TitleAction;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketUtils {

    private static final UUID SYSTEM_SENDER = new UUID(0L, 0L);

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComponent = toComponent(title);
        Component subtitleComponent = toComponent(subtitle);

        if (serverVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
            sendPackets(player,
                    new WrapperPlayServerSetTitleTimes(fadeIn, stay, fadeOut),
                    new WrapperPlayServerSetTitleSubtitle(subtitleComponent),
                    new WrapperPlayServerSetTitleText(titleComponent));
            return;
        }

        sendPackets(player,
                new WrapperPlayServerTitle(TitleAction.SET_TIMES_AND_DISPLAY, (Component) null, null, null, fadeIn, stay, fadeOut),
                new WrapperPlayServerTitle(TitleAction.SET_SUBTITLE, null, subtitleComponent, null, 0, 0, 0),
                new WrapperPlayServerTitle(TitleAction.SET_TITLE, titleComponent, null, null, 0, 0, 0));
    }

    public static void sendBar(Player player, String message) {
        Component component = toComponent(message);
        ServerVersion version = serverVersion();

        if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            sendPackets(player, new WrapperPlayServerActionBar(component));
            return;
        }

        ChatMessage chatMessage = version.isNewerThanOrEquals(ServerVersion.V_1_16)
                ? new ChatMessage_v1_16(component, ChatTypes.GAME_INFO, SYSTEM_SENDER)
                : new ChatMessageLegacy(component, ChatTypes.GAME_INFO);
        sendPackets(player, new WrapperPlayServerChatMessage(chatMessage));
    }

    public static void sendPackets(Player player, PacketWrapper<?>... packets) {
        for (PacketWrapper<?> packet : packets) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }

    private static Component toComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(StringUtils.color(text));
    }

    private static ServerVersion serverVersion() {
        return PacketEvents.getAPI().getServerManager().getVersion();
    }

}
