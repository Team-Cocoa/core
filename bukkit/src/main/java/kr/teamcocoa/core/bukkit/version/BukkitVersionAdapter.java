package kr.teamcocoa.core.bukkit.version;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface BukkitVersionAdapter {

    ItemStack createCustomHead(String textureValue);

    ItemStack createGUIBackground();

    void setDisplayName(ItemMeta itemMeta, String displayName);

    void setLore(ItemMeta itemMeta, List<String> lore);

    boolean displayNameEquals(ItemMeta itemMeta, String displayName);

    void setItemTag(ItemMeta itemMeta, String tag);

    String getItemTag(ItemMeta itemMeta);

    void setGameRule(World world, BooleanGameRule rule, boolean value);

    boolean hasLegacyTextLimits();

    Sound getNoteBlockPlingSound();

    double[] getTPS();

}
