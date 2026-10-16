package kr.teamcocoa.core.bukkit.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import kr.teamcocoa.core.utils.Pair;
import kr.teamcocoa.core.utils.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemUtils {

    public static final String UNKNOWN_PLAYER_HEAD_VALUE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkMjAzMzBkYTU5YzIwN2Q3ODM1MjgzOGU5MWE0OGVhMWU0MmI0NWE5ODkzMjI2MTQ0YjI1MWZlOWI5ZDUzNSJ9fX0=";

    public static ItemStack getCustomHead(String value) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", value));
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack getGUIBackGround() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.empty());
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack name(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(StringUtils.color(name)));
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

        List<Component> componentLore = new ArrayList<>(lore.size());
        for (String string : lore) {
            componentLore.add(Component.text(StringUtils.color(string)));
        }
        itemMeta.lore(componentLore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean equalsTitle(ItemStack itemStack, String title) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return ComponentUtils.componentEquals(itemMeta.displayName(), title);
    }

}
