package com.fq.rpc.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtil {
    private static final Map<String, ExecutorService> THREAD_POOL_CACHE = new ConcurrentHashMap<>();
    // cpu数量
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_KEEP_ALIVE_TIME = 60;
    private static final int DEFAULT_QUEUESIZE = 128;

    // io密集
    private static final int IO_INTENSIVE_NUM = CPU_NUM * 2;
    // cpu密集
    private static final int CPU_INTENSIVE_NUM = CPU_NUM + 1;


    public static ExecutorService createCpuIntensiveThreadPool(
            String poolName
    ) {
        return createThreadPool(CPU_INTENSIVE_NUM, CPU_INTENSIVE_NUM, poolName);
    }

    public static ExecutorService createIoIntensiveThreadPool(
            String poolName
    ) {
        return createThreadPool(IO_INTENSIVE_NUM, IO_INTENSIVE_NUM, poolName);
    }


    public static ExecutorService createThreadPool(
            int corePoolSize,
            int maxPoolSize,
            String poolName
    ) {
        return createThreadPool(
                corePoolSize,
                maxPoolSize,
                DEFAULT_KEEP_ALIVE_TIME,
                DEFAULT_QUEUESIZE,
                poolName,
                false
        );
    }

    public static ExecutorService createThreadPool(
            int corePoolSize,
            int maxPoolSize,
            long keepAliveTime,
            int queueSize,
            String poolName,
            boolean isDaemon
    ) {
        if (THREAD_POOL_CACHE.containsKey(poolName)){
            return THREAD_POOL_CACHE.get(poolName);
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                createThreadFactory(poolName, isDaemon)
        );
        THREAD_POOL_CACHE.put(poolName, threadPoolExecutor);
        return threadPoolExecutor;
    }

    public static ThreadFactory createThreadFactory(String poolName){
        return createThreadFactory(poolName, false);
    }
    public static ThreadFactory createThreadFactory(String poolName, boolean isDaemon){
        // 是否是守护线程
        ThreadFactoryBuilder threadFactoryBuilder = ThreadFactoryBuilder.create().setDaemon(isDaemon);

        if(StrUtil.isBlankIfStr(poolName)){
            return Executors.defaultThreadFactory();
        }
        return threadFactoryBuilder.setNamePrefix(poolName).build();
    }

    public static void closeAll(){
        THREAD_POOL_CACHE.entrySet().parallelStream().forEach(entry -> {
            String poolName = entry.getKey();
            ExecutorService executorService = entry.getValue();
            log.info("{}, 线程池开始停止...", poolName);
            executorService.shutdown();
            try {
                if (executorService.awaitTermination(10, TimeUnit.SECONDS)){
                    log.info("线程池已停止: {}", poolName);
                }else {
                    log.info("线程池停止超时，正在强行停止: {}", poolName);
                    executorService.shutdownNow();
                }
            } catch (Exception e) {
                log.error("线程池停止异常: {}, {}",poolName ,e.getMessage(), e);
                executorService.shutdownNow();
            }
        });
    }
}
