package kr.teamcocoa.core.bukkit.menu;

import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import kr.teamcocoa.core.bukkit.utils.TextLimit;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A chest menu identified by its holder rather than its title, with one click handler per slot.
 * Clicks anywhere in the view are cancelled while it is open.
 */
public class Menu implements InventoryHolder {

    private final Inventory inventory;

    private final Map<Integer, MenuClickHandler> handlers = new HashMap<>();

    public Menu(int rows, String title) {
        this.inventory = Bukkit.createInventory(this, rows * 9, TextLimit.INVENTORY_TITLE.truncate(StringUtils.color(title)));
    }

    public void setItem(int slot, ItemStack itemStack) {
        setItem(slot, itemStack, null);
    }

    public void setItem(int slot, ItemStack itemStack, MenuClickHandler handler) {
        inventory.setItem(slot, itemStack);
        if (handler == null) {
            handlers.remove(slot);
        } else {
            handlers.put(slot, handler);
        }
    }

    public void fillBackground() {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (inventory.getItem(slot) == null) {
                inventory.setItem(slot, ItemUtils.getGUIBackGround());
            }
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    protected void onClose(Player player) {
    }

    void handleClick(Player player, InventoryClickEvent event) {
        MenuClickHandler handler = handlers.get(event.getRawSlot());
        if (handler != null) {
            handler.onClick(player, event);
        }
    }

    void handleClose(Player player) {
        onClose(player);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
