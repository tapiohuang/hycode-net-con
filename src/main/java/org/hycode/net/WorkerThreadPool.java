package org.hycode.net;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerThreadPool {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
    private static final int keepAliveTime = 30;
    public static ThreadPoolExecutor POOL;

    static {
        POOL = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DefaultThreadFactory("工作线程"), new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }

}
