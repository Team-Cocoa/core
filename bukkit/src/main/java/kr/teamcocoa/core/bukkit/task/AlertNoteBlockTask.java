package kr.teamcocoa.core.bukkit.task;

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
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1F, 100F);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 13F, 100F);
        i++;
    }

    public void start(JavaPlugin plugin) {
        runTaskTimer(plugin, 0, 2L);
    }

}
