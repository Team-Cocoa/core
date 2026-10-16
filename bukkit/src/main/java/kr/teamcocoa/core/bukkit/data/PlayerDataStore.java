package kr.teamcocoa.core.bukkit.data;

public interface PlayerDataStore<T extends Versioned> extends PlayerDataSource<T> {

    /**
     * Called off the main thread, never concurrently for the same player. Only called when the data
     * is edited; the edited flag is cleared by the manager after this returns true.
     */
    boolean save(T data) throws Exception;

}
