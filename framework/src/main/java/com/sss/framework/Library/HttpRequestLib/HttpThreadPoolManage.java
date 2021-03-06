package com.sss.framework.Library.HttpRequestLib;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Http线程池管理类
 */
public class HttpThreadPoolManage {
    private static HttpThreadPoolManage threadPoolManage;
    /**
     * 核心线程数
     */
    private int corePoolSize = 5;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 10;
    /**
     * 线程超时重试时间
     */
    private long timeOut = 30 * 1000;
    /**
     * 队列容量
     */
    private int capacity = 5;
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 请求队列
     */
    private LinkedBlockingQueue queue = new LinkedBlockingQueue();

    public static HttpThreadPoolManage getInstance() {
        if (threadPoolManage == null) {
            threadPoolManage = new HttpThreadPoolManage();
        }
        return threadPoolManage;

    }


    private HttpThreadPoolManage() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, timeOut, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(capacity));
        threadPoolExecutor.execute(workThread);
    }

    /**
     * 执行线程任务
     *
     * @param runnable
     * @return
     */
    public HttpThreadPoolManage execute(Runnable runnable) {
        if (runnable != null) {
            try {
                queue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return threadPoolManage;
    }

    /**
     * 用来处理超时任务
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            executor.execute(r);
        }
    };
    /**
     * 主工作线程，负责从请求队列拿出线程挨个执行
     */
    private Runnable workThread = new Runnable() {
        @Override
        public void run() {
            while (true) {
                Runnable runnable = null;
                try {
                    runnable = (Runnable) queue.take();
                    rejectedExecutionHandler.rejectedExecution(runnable,threadPoolExecutor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (runnable != null) {
                    runnable.run();
                }

            }
        }
    };

    /**
     * 设置核心线程数
     *
     * @param corePoolSize
     */
    public HttpThreadPoolManage setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return threadPoolManage;
    }

    /**
     * 设置最大线程数
     *
     * @param maxPoolSize
     */
    public HttpThreadPoolManage setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return threadPoolManage;
    }

    /**
     * 设置线程超时重试时间
     *
     * @param timeOut
     */
    public HttpThreadPoolManage setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return threadPoolManage;
    }

    /**
     * 设置队列容量
     *
     * @param capacity
     */
    public HttpThreadPoolManage setCapacity(int capacity) {
        this.capacity = capacity;
        return threadPoolManage;
    }




}
