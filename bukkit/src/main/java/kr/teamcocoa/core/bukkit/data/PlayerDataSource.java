package kr.teamcocoa.core.bukkit.data;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerDataSource<T> {

    /**
     * Called off the main thread. Throw {@link PlayerDataLoadException} to kick the player with a
     * specific message.
     */
    T load(Player player) throws Exception;

}
