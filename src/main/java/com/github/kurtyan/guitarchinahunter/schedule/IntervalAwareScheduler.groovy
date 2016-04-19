package com.github.kurtyan.guitarchinahunter.schedule

import groovy.util.logging.Slf4j

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by yanke on 2016/4/19.
 */
@Slf4j
class IntervalAwareScheduler {

    def IntervalConfigSet intervalConfigSet
    def long defaultIntervalInMillis
    def pool = Executors.newScheduledThreadPool(5)

    public IntervalAwareScheduler(IntervalConfigSet intervalConfigSet, long defaultIntervalInMillis) {
        this.intervalConfigSet = intervalConfigSet
        this.defaultIntervalInMillis = defaultIntervalInMillis
    }

    def submit(Runnable runnable) {
        pool.scheduleAtFixedRate(
                new IntervalAwareRunableWapper(
                        configList: intervalConfigSet.configList,
                        command: runnable,
                        defaultIntervalInMillis: defaultIntervalInMillis
                ),
                0,
                1000L,
                TimeUnit.MILLISECONDS
        )
    }

}
