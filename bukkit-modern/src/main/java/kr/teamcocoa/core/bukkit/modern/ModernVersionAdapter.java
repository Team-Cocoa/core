package kr.teamcocoa.core.bukkit.modern;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import kr.teamcocoa.core.bukkit.version.BooleanGameRule;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModernVersionAdapter implements BukkitVersionAdapter {

    private final NamespacedKey itemTagKey = new NamespacedKey(BukkitCoreBootstrap.getInstance(), "item_tag");

    @Override
    public ItemStack createCustomHead(String textureValue) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", textureValue));
        meta.setPlayerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public ItemStack createGUIBackground() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.empty());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void setDisplayName(ItemMeta itemMeta, String displayName) {
        itemMeta.displayName(Component.text(displayName));
    }

    @Override
    public void setLore(ItemMeta itemMeta, List<String> lore) {
        List<Component> componentLore = new ArrayList<>(lore.size());
        for (String string : lore) {
            componentLore.add(Component.text(string));
        }
        itemMeta.lore(componentLore);
    }

    @Override
    public boolean displayNameEquals(ItemMeta itemMeta, String displayName) {
        return itemMeta.displayName() instanceof TextComponent textComponent
                && textComponent.content().equals(displayName);
    }

    @Override
    public void setItemTag(ItemMeta itemMeta, String tag) {
        if (tag == null) {
            itemMeta.getPersistentDataContainer().remove(itemTagKey);
            return;
        }
        itemMeta.getPersistentDataContainer().set(itemTagKey, PersistentDataType.STRING, tag);
    }

    @Override
    public String getItemTag(ItemMeta itemMeta) {
        return itemMeta.getPersistentDataContainer().get(itemTagKey, PersistentDataType.STRING);
    }

    // org.bukkit.GameRules replaces these from 1.21.11, but this adapter also runs on 1.13 to 1.21.10
    @Override
    @SuppressWarnings("removal")
    public void setGameRule(World world, BooleanGameRule rule, boolean value) {
        GameRule<Boolean> gameRule = switch (rule) {
            case DAYLIGHT_CYCLE -> GameRule.DO_DAYLIGHT_CYCLE;
            case ANNOUNCE_ADVANCEMENTS -> GameRule.ANNOUNCE_ADVANCEMENTS;
            case LOG_ADMIN_COMMANDS -> GameRule.LOG_ADMIN_COMMANDS;
        };
        world.setGameRule(gameRule, value);
    }

    @Override
    public boolean hasLegacyTextLimits() {
        return false;
    }

    @Override
    public Sound getNoteBlockPlingSound() {
        return Sound.BLOCK_NOTE_BLOCK_PLING;
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getTPS();
    }

}
