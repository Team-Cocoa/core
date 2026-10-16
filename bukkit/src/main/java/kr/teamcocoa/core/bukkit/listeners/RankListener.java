package kr.teamcocoa.core.bukkit.listeners;

import kr.teamcocoa.core.bukkit.rank.ChatFormatService;
import kr.teamcocoa.core.bukkit.rank.NameTagService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RankListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        if (!ChatFormatService.isEnabled()) {
            return;
        }
        Player player = e.getPlayer();
        e.setFormat(ChatFormatService.format(player));
        e.setMessage(ChatFormatService.formatMessage(player, e.getMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        NameTagService.remove(e.getPlayer());
    }

}
