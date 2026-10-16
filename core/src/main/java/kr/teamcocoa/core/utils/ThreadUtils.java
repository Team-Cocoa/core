package kr.teamcocoa.core.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadUtils {

    public static ThreadPoolExecutor getThreadPool(int maximum, long keepAliveTime, TimeUnit unit, String poolName) {

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("Cocoa-" + poolName + "-%d").build();

        // ThreadPoolExecutor only grows past the core size when the queue is full, so with an
        // unbounded queue the core size has to be the maximum
        ThreadPoolExecutor pool = new ThreadPoolExecutor(maximum, maximum, keepAliveTime, unit, new LinkedBlockingQueue<>(), factory);

        if (keepAliveTime > 0) {
            pool.allowCoreThreadTimeOut(true);
        }

        return pool;
    }

}
