package kr.teamcocoa.core.bukkit.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerActionBar;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketUtils {

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendPackets(player,
                new WrapperPlayServerSetTitleTimes(fadeIn, stay, fadeOut),
                new WrapperPlayServerSetTitleText(Component.text(StringUtils.color(title))),
                new WrapperPlayServerSetTitleSubtitle(Component.text(StringUtils.color(subtitle))));
    }

    public static void sendBar(Player player, String message) {
        sendPackets(player, new WrapperPlayServerActionBar(Component.text(message)));
    }

    public static void sendPackets(Player player, PacketWrapper<?>... packets) {
        for (PacketWrapper<?> packet : packets) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }

}
