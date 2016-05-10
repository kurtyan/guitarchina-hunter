package com.github.kurtyan.guitarchinahunter.schedule;

import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalAwareScheduler(val intervalConfigSet: IntervalConfigSet, val defaultIntervalInMillis: Long) {

    val logger = LoggerFactory.getLogger(javaClass)
    val pool = Executors.newScheduledThreadPool(5);

    fun submit(runnable: Runnable): Future<*> {
        val wrapper = IntervalAwareRunableWapper(
                intervalConfigSet.configList,
                defaultIntervalInMillis,
                runnable
        )
        return pool.scheduleAtFixedRate(
                wrapper,
                0,
                1000L,
                TimeUnit.MILLISECONDS
        )
    }

}
