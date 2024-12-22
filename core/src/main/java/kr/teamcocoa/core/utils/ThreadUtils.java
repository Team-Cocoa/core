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

        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, maximum, keepAliveTime, unit, new LinkedBlockingQueue<>(maximum));

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("Cocoa-" + poolName + "-%d").build();

        pool.setThreadFactory(factory);

        return pool;
    }
    
}
