package kr.teamcocoa.core.bukkit.packet;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Only sends packets, so it is safe to call off the main thread.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockAnimations {

    public static final int MAX_STAGE = 9;

    private static final byte CLEAR_STAGE = -1;

    public static int newAnimationId() {
        return ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    }

    public static void crack(Block block, int animationId, int stage) {
        crack(Bukkit.getOnlinePlayers(), block, animationId, stage);
    }

    public static void crack(Iterable<? extends Player> viewers, Block block, int animationId, int stage) {
        if (stage < 0 || stage > MAX_STAGE) {
            throw new IllegalArgumentException("Crack stage must be between 0 and " + MAX_STAGE + ": " + stage);
        }
        send(viewers, block, animationId, (byte) stage);
    }

    public static void clear(Block block, int animationId) {
        clear(Bukkit.getOnlinePlayers(), block, animationId);
    }

    public static void clear(Iterable<? extends Player> viewers, Block block, int animationId) {
        send(viewers, block, animationId, CLEAR_STAGE);
    }

    private static void send(Iterable<? extends Player> viewers, Block block, int animationId, byte stage) {
        WrapperPlayServerBlockBreakAnimation packet = new WrapperPlayServerBlockBreakAnimation(
                animationId, new Vector3i(block.getX(), block.getY(), block.getZ()), stage);
        for (Player viewer : viewers) {
            PacketUtils.sendPackets(viewer, packet);
        }
    }

}
