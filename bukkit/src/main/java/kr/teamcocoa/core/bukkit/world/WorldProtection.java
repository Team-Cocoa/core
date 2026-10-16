package kr.teamcocoa.core.bukkit.world;

import kr.teamcocoa.core.bukkit.main.BukkitCoreBootstrap;
import kr.teamcocoa.core.bukkit.version.BooleanGameRule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.Map;

/**
 * World rules every lobby and minigame server shares, applied to every loaded world:
 * {@code WorldProtection.create().noWeather().noHunger().fixedTime(0).enable(plugin)}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorldProtection implements Listener {

    private boolean noWeather;
    private boolean noHunger;
    private boolean noBeds;
    private boolean noPickup;
    private boolean unloadSpawnChunks;
    private Long fixedTime;
    private final Map<BooleanGameRule, Boolean> gameRules = new EnumMap<>(BooleanGameRule.class);

    public static WorldProtection create() {
        return new WorldProtection();
    }

    public WorldProtection noWeather() {
        this.noWeather = true;
        return this;
    }

    public WorldProtection noHunger() {
        this.noHunger = true;
        return this;
    }

    public WorldProtection noBeds() {
        this.noBeds = true;
        return this;
    }

    public WorldProtection noPickup() {
        this.noPickup = true;
        return this;
    }

    public WorldProtection unloadSpawnChunks() {
        this.unloadSpawnChunks = true;
        return this;
    }

    public WorldProtection fixedTime(long time) {
        this.fixedTime = time;
        return gameRule(BooleanGameRule.DAYLIGHT_CYCLE, false);
    }

    public WorldProtection gameRule(BooleanGameRule rule, boolean value) {
        gameRules.put(rule, value);
        return this;
    }

    public void enable(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (World world : Bukkit.getWorlds()) {
            apply(world);
        }
    }

    private void apply(World world) {
        if (unloadSpawnChunks) {
            world.setKeepSpawnInMemory(false);
        }
        if (noWeather) {
            world.setStorm(false);
            world.setThundering(false);
        }
        for (Map.Entry<BooleanGameRule, Boolean> entry : gameRules.entrySet()) {
            BukkitCoreBootstrap.getVersionAdapter().setGameRule(world, entry.getKey(), entry.getValue());
        }
        if (fixedTime != null) {
            world.setTime(fixedTime);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInit(WorldInitEvent e) {
        if (unloadSpawnChunks) {
            e.getWorld().setKeepSpawnInMemory(false);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        apply(e.getWorld());
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent e) {
        if (noWeather && e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (noHunger && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (noBeds) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent e) {
        if (noPickup) {
            e.setCancelled(true);
        }
    }

}
