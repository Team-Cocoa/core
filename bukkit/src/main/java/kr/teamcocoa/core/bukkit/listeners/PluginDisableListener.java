package kr.teamcocoa.core.bukkit.listeners;

import kr.teamcocoa.core.bukkit.task.PluginExecutors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginDisableListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent e) {
        PluginExecutors.shutdown(e.getPlugin());
    }

}
