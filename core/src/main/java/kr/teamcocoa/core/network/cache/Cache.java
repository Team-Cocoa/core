package kr.teamcocoa.core.network.cache;

import kr.teamcocoa.core.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Cache<K, V> {

    private static final long NO_EXPIRY = -1;

    private ConcurrentHashMap<K, Pair<V, Long>> cache = new ConcurrentHashMap<>();

    private long validDuration;

    public Cache() {
        this.validDuration = NO_EXPIRY;
    }

    public Cache(int duration, TimeUnit timeUnit) {
        this.validDuration = timeUnit.toMillis(duration);
    }

    public void createData(K key, V value) {
        putIfAbsent(key, value);
    }

    /**
     * @return the value already cached for the key, or null if the given value was stored
     */
    public V putIfAbsent(K key, V value) {
        AtomicReference<V> existing = new AtomicReference<>();
        cache.compute(key, (k, old) -> {
            if (old != null && !isExpired(old)) {
                existing.set(old.getFirst());
                return old;
            }
            return new Pair<>(value, System.currentTimeMillis());
        });
        return existing.get();
    }

    public V readData(K key) {
        Pair<V, Long> pair = cache.get(key);
        if(pair == null) {
            return null;
        }
        if(isExpired(pair)) {
            cache.remove(key, pair);
            return null;
        }
        return pair.getFirst();
    }

    public void updateData(K key, V value) {
        cache.computeIfPresent(key, (k, old) -> isExpired(old) ? null : new Pair<>(value, System.currentTimeMillis()));
    }

    public void deleteData(K key) {
        cache.remove(key);
    }

    /**
     * @return the removed value, or null if there was no valid value for the key
     */
    public V remove(K key) {
        Pair<V, Long> old = cache.remove(key);
        if (old == null || isExpired(old)) {
            return null;
        }
        return old.getFirst();
    }

    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Pair<V, Long> pair : cache.values()) {
            if (!isExpired(pair)) {
                values.add(pair.getFirst());
            }
        }
        return values;
    }

    public void evictExpired() {
        cache.values().removeIf(this::isExpired);
    }

    public void clearCache() {
        cache.clear();
    }

    private boolean isExpired(Pair<V, Long> pair) {
        return validDuration != NO_EXPIRY && System.currentTimeMillis() - pair.getSecond() > validDuration;
    }

}
