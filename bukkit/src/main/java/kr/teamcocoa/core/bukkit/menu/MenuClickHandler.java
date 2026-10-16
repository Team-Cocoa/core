package kr.teamcocoa.core.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface MenuClickHandler {

    /**
     * The event is already cancelled.
     */
    void onClick(Player player, InventoryClickEvent event);

}
