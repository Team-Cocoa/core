package kr.teamcocoa.core.test.utils;

import kr.teamcocoa.core.utils.ThreadUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ThreadUtils 테스트")
class ThreadUtilsTest {

    @Test
    @DisplayName("스레드 수의 두 배를 넘는 작업도 거부하지 않고 모두 실행한다")
    void 작업_거부_없음_테스트() throws InterruptedException {
        ThreadPoolExecutor pool = ThreadUtils.getThreadPool(2, 1, TimeUnit.SECONDS, "test");
        CountDownLatch release = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                try {
                    release.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                done.countDown();
            });
        }
        assertTrue(pool.getPoolSize() == 2);
        release.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS));
        pool.shutdown();
    }

}
