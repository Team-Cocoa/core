package kr.teamcocoa.core.bukkit.listeners;

import kr.teamcocoa.core.bukkit.events.anticheat.AntiCheatFlag;
import kr.teamcocoa.core.bukkit.events.anticheat.AntiCheatFlagEvent;
import kr.teamcocoa.core.bukkit.events.anticheat.AntiCheatPunishEvent;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiCheatListener implements Listener {

    @EventHandler
    public void onFlag(VulcanFlagEvent e) {
        Player player = e.getPlayer();
        Check rawCheck = e.getCheck();
        double tps = Bukkit.getTPS()[0];
        AntiCheatFlag flag = new AntiCheatFlag(
                rawCheck.getName(),
                Character.toString(rawCheck.getType()),
                rawCheck.getVl(),
                rawCheck.getMaxVl());
        AntiCheatFlagEvent event = new AntiCheatFlagEvent(player, flag, tps);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler
    public void onPunish(VulcanPunishEvent e) {
        Player player = e.getPlayer();
        AntiCheatPunishEvent event = new AntiCheatPunishEvent(player);
        Bukkit.getPluginManager().callEvent(event);
    }

}
