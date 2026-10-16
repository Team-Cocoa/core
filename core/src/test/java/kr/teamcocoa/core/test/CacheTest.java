package kr.teamcocoa.core.test;

import kr.teamcocoa.core.network.cache.Cache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("캐시 테스트")
class CacheTest {

    static Cache<String, String> cache = new Cache<>(1, TimeUnit.SECONDS);

    @AfterEach
    void 에뿌따이쮜() {
        cache.clearCache();
    }

    @Test
    @DisplayName("값이 정상적으로 들어가는지 테스트 하기")
    void 값_생성_테스트() {
        cache.createData("a", "abc");
        assertEquals(cache.readData("a"), "abc");
    }

    @Test
    @DisplayName("값이 1초뒤에 정상적으로 사라지는지 테스트 하기")
    void 데이터_시간제한_테스트() {
        cache.createData("a", "abc");
        try {
            Thread.sleep(1500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(cache.readData("a"));
    }

    @Test
    @DisplayName("값이 정상적으로 삭제 되는지 테스트 하기")
    void 값_삭제_테스트() {
        cache.createData("a", "abc");
        cache.deleteData("a");
        assertNull(cache.readData("a"));
    }

    @Test
    @DisplayName("값을 업데이트 하는 테스트")
    void 값_업데이트_테스트() {
        cache.createData("a", "abc");
        cache.updateData("a", "bcd");
        assertEquals(cache.readData("a"), "bcd");
        cache.updateData("b", "abc");
        assertNull(cache.readData("b"));
    }

    @Test
    @DisplayName("putIfAbsent는 기존 값을 반환하고 덮어쓰지 않는다")
    void putIfAbsent_테스트() {
        assertNull(cache.putIfAbsent("a", "abc"));
        assertEquals("abc", cache.putIfAbsent("a", "bcd"));
        assertEquals("abc", cache.readData("a"));
    }

    @Test
    @DisplayName("remove는 삭제된 값을 반환한다")
    void remove_테스트() {
        cache.createData("a", "abc");
        assertEquals("abc", cache.remove("a"));
        assertNull(cache.remove("a"));
    }

    @Test
    @DisplayName("values는 만료되지 않은 값만 반환한다")
    void values_테스트() throws InterruptedException {
        cache.createData("a", "abc");
        Thread.sleep(1500);
        cache.createData("b", "bcd");
        assertEquals(List.of("bcd"), List.copyOf(cache.values()));
        assertEquals("bcd", cache.putIfAbsent("b", "x"));
        assertNull(cache.putIfAbsent("a", "new"));
    }

    @Test
    @DisplayName("만료 없는 캐시는 값을 유지한다")
    void 만료없음_테스트() throws InterruptedException {
        Cache<String, String> noExpiry = new Cache<>();
        noExpiry.createData("a", "abc");
        Thread.sleep(50);
        noExpiry.evictExpired();
        assertEquals("abc", noExpiry.readData("a"));
    }
}

