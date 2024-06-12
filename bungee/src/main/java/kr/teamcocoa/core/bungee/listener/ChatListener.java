package kr.teamcocoa.core.bungee.listener;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        String message = e.getMessage();
        if(!e.isCommand()) {
            return;
        }

        if(e.isCommand() && message.split(" ")[0].contains(":")) {
            e.setCancelled(true);
        }
    }

}
