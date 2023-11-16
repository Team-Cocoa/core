package kr.teamcocoa.core.network.cache;

import kr.teamcocoa.core.utils.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Cache<K, V> {

    private ConcurrentHashMap<K, Pair<V, Long>> cache = new ConcurrentHashMap<>();

    private long validDuration;

    public Cache(int duration, TimeUnit timeUnit) {
        this.validDuration = timeUnit.toMillis(duration);
    }

    public void createData(K key, V value) {
        if(cache.containsKey(key)) {
            if(readData(key) != null) {
                return;
            }
        }
        cache.put(key, new Pair<>(value, System.currentTimeMillis()));
    }

    public V readData(K key) {
        Pair<V, Long> pair = cache.getOrDefault(key, null);
        if(pair == null) {
            return null;
        }
        if(System.currentTimeMillis() - pair.getSecond() > validDuration) {
            return null;
        }
        return pair.getFirst();
    }

    public void updateData(K key, V value) {
        if(!cache.containsKey(key)) {
            return;
        }
        cache.put(key, new Pair<>(value, System.currentTimeMillis()));
    }

    public void deleteData(K key) {
        if(!cache.containsKey(key)) {
            return;
        }
        cache.remove(key);
    }

}
