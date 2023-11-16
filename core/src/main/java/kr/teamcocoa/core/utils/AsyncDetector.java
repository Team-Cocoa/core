package kr.teamcocoa.core.utils;

import lombok.Setter;

public class AsyncDetector {

    @Setter
    private static Thread mainThread;

    public static void catchSynchronous() {
        if(Thread.currentThread() == mainThread) {
            throw new IllegalStateException("Synchronous Called!");
        }
    }

    public static void catchAsynchronous() {
        if(Thread.currentThread() != mainThread) {
            throw new IllegalStateException("Asynchronous Called!");
        }
    }

}
