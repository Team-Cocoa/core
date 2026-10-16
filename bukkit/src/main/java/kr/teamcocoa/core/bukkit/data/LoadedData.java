package kr.teamcocoa.core.bukkit.data;

import java.util.Map;

public class LoadedData {

    private final Map<PlayerDataSource<?>, Object> values;

    LoadedData(Map<PlayerDataSource<?>, Object> values) {
        this.values = values;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(PlayerDataSource<T> source) {
        if (!values.containsKey(source)) {
            throw new IllegalArgumentException("Source is not registered: " + source);
        }
        return (T) values.get(source);
    }

}
