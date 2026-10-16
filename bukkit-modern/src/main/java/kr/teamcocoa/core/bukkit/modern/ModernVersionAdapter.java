package kr.teamcocoa.core.bukkit.modern;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import kr.teamcocoa.core.bukkit.version.BukkitVersionAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModernVersionAdapter implements BukkitVersionAdapter {

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
    public Sound getNoteBlockPlingSound() {
        return Sound.BLOCK_NOTE_BLOCK_PLING;
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getTPS();
    }

}
