package kr.teamcocoa.core.bukkit.legacy;

import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class LegacyVersionAdapter implements BukkitVersionAdapter {

    private static final short PLAYER_SKULL_DATA = 3;
    private static final short GRAY_GLASS_PANE_DATA = 7;

    @Override
    public ItemStack createCustomHead(String textureValue) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, PLAYER_SKULL_DATA);
        String skullOwner = "{SkullOwner:{Id:\"" + UUID.randomUUID() + "\",Properties:{textures:[{Value:\"" + textureValue + "\"}]}}}";
        return Bukkit.getUnsafe().modifyItemStack(head, skullOwner);
    }

    @Override
    public ItemStack createGUIBackground() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, GRAY_GLASS_PANE_DATA);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void setDisplayName(ItemMeta itemMeta, String displayName) {
        itemMeta.setDisplayName(displayName);
    }

    @Override
    public void setLore(ItemMeta itemMeta, List<String> lore) {
        itemMeta.setLore(lore);
    }

    @Override
    public boolean displayNameEquals(ItemMeta itemMeta, String displayName) {
        return itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(displayName);
    }

    @Override
    public Sound getNoteBlockPlingSound() {
        return Sound.NOTE_PLING;
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getServer().spigot().getTPS();
    }

}
