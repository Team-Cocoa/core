package kr.teamcocoa.core.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

// Uses event.getInventory() (the top inventory) instead of the InventoryView, which is a class in
// 1.8 and an interface on modern servers, so calls on it from this jar would not link on both
public class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(InventoryClickEvent e) {
        Inventory top = e.getInventory();
        if (!(top.getHolder() instanceof Menu menu)) {
            return;
        }
        e.setCancelled(true);

        if (e.getRawSlot() < 0 || e.getRawSlot() >= top.getSize() || !(e.getWhoClicked() instanceof Player player)) {
            return;
        }
        menu.handleClick(player, e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof Menu) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Menu menu && e.getPlayer() instanceof Player player) {
            menu.handleClose(player);
        }
    }

}
