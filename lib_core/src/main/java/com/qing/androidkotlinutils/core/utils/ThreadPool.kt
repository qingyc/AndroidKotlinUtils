package com.qing.androidkotlinutils.core.utils

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * 类说明: 线程池
 *
 * @author qing
 * @time 2018/5/25 12:02
 */
object ThreadPool {

    /**
     * 核心线程数
     */
    const val CORE_POOL_SIZE = 3
    /**
     * 最大线程
     */
    const val MAX_POOL_SIZE = 20
    /**
     * alive时间
     */
    const val KEEP_ALIVE = 10L


    private val sThreadFactory = object : ThreadFactory {
        private val mCount = AtomicInteger()

        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, "download_task#" + mCount.getAndIncrement())
        }
    }

    private var mThreadPoolExecutor: ThreadPoolExecutor

    init {
        mThreadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, LinkedBlockingDeque<Runnable>(), sThreadFactory)
    }

    fun getThreadPoolExecutor(): ThreadPoolExecutor {
        return mThreadPoolExecutor
    }


}