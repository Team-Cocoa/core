package kr.teamcocoa.core.bukkit.task;

import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class AlertNoteBlockTask extends BukkitRunnable {

    private Player player;
    private int i = 0;

    public AlertNoteBlockTask(Player player) {
        this.player = player;
    }

    public static AlertNoteBlockTask newInstance(Player player) {
        return new AlertNoteBlockTask(player);
    }

    @Override
    public void run() {
        if(i > 20) {
            cancel();
            return;
        }
        Sound sound = BukkitCoreBootstrap.getVersionAdapter().getNoteBlockPlingSound();
        player.playSound(player.getLocation(), sound, 1F, 100F);
        player.playSound(player.getLocation(), sound, 13F, 100F);
        i++;
    }

    public void start(JavaPlugin plugin) {
        runTaskTimer(plugin, 0, 2L);
    }

}
