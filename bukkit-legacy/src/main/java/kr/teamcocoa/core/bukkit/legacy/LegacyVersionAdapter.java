package kr.teamcocoa.core.bukkit.legacy;

import kr.teamcocoa.core.bukkit.version.BooleanGameRule;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LegacyVersionAdapter implements BukkitVersionAdapter {

    private static final short PLAYER_SKULL_DATA = 3;
    private static final short GRAY_GLASS_PANE_DATA = 7;

    // 1.8 has no PersistentDataContainer, so the tag is kept in a lore line where every
    // character is preceded by a color char, which the client renders as nothing
    private static final String TAG_MARKER = "\u00a7t\u00a7c\u00a7t";

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
        String tagLine = findTagLine(itemMeta);
        if (tagLine == null) {
            itemMeta.setLore(lore);
            return;
        }
        List<String> taggedLore = new ArrayList<>(lore);
        taggedLore.add(tagLine);
        itemMeta.setLore(taggedLore);
    }

    @Override
    public boolean displayNameEquals(ItemMeta itemMeta, String displayName) {
        return itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(displayName);
    }

    @Override
    public void setItemTag(ItemMeta itemMeta, String tag) {
        List<String> lore = itemMeta.hasLore() ? new ArrayList<>(itemMeta.getLore()) : new ArrayList<>();
        lore.removeIf(line -> line.startsWith(TAG_MARKER));
        if (tag != null) {
            StringBuilder line = new StringBuilder(TAG_MARKER);
            for (char c : tag.toCharArray()) {
                line.append('\u00a7').append(c);
            }
            lore.add(line.toString());
        }
        itemMeta.setLore(lore.isEmpty() ? null : lore);
    }

    @Override
    public String getItemTag(ItemMeta itemMeta) {
        String line = findTagLine(itemMeta);
        if (line == null) {
            return null;
        }
        StringBuilder tag = new StringBuilder();
        for (int i = TAG_MARKER.length() + 1; i < line.length(); i += 2) {
            tag.append(line.charAt(i));
        }
        return tag.toString();
    }

    @Override
    public void setGameRule(World world, BooleanGameRule rule, boolean value) {
        world.setGameRuleValue(rule.getLegacyName(), Boolean.toString(value));
    }

    @Override
    public boolean hasLegacyTextLimits() {
        return true;
    }

    @Override
    public Sound getNoteBlockPlingSound() {
        return Sound.NOTE_PLING;
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getServer().spigot().getTPS();
    }

    private static String findTagLine(ItemMeta itemMeta) {
        if (!itemMeta.hasLore()) {
            return null;
        }
        for (String line : itemMeta.getLore()) {
            if (line.startsWith(TAG_MARKER)) {
                return line;
            }
        }
        return null;
    }

}
