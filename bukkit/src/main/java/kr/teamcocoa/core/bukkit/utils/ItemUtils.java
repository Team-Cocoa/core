package kr.teamcocoa.core.bukkit.utils;

import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import kr.teamcocoa.core.utils.Pair;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static final String UNKNOWN_PLAYER_HEAD_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0=";

    public static ItemStack getCustomHead(String value) {
        return BukkitCoreBootstrap.getVersionAdapter().createCustomHead(value);
    }

    public static ItemStack getGUIBackGround() {
        return BukkitCoreBootstrap.getVersionAdapter().createGUIBackground();
    }

    public static ItemStack name(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        BukkitCoreBootstrap.getVersionAdapter().setDisplayName(itemMeta, StringUtils.color(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack enchant(ItemStack itemStack, List<Pair<Enchantment, Integer>> enchantments) {
        for (Pair<Enchantment, Integer> enchantment : enchantments) {
            itemStack.addEnchantment(enchantment.getFirst(), enchantment.getSecond());
        }
        return itemStack;
    }

    public static ItemStack lore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> coloredLore = new ArrayList<>(lore.size());
        for (String string : lore) {
            coloredLore.add(StringUtils.color(string));
        }
        BukkitCoreBootstrap.getVersionAdapter().setLore(itemMeta, coloredLore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean equalsTitle(ItemStack itemStack, String title) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return BukkitCoreBootstrap.getVersionAdapter().displayNameEquals(itemMeta, StringUtils.color(title));
    }

}
