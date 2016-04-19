package com.github.kurtyan.guitarchinahunter.schedule

import groovy.util.logging.Slf4j

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by yanke on 2016/4/19.
 */
@Slf4j
class IntervalAwareRunableWapper implements Runnable {

    def List<IntervalConfig> configList
    def Runnable command
    def long defaultIntervalInMillis
    def Closure<Long> invokeTimeProvider

    private long lastRanInMillis = 0L

    private synchronized Closure<Long> getInvokeTimeProvider() {
        if (invokeTimeProvider == null) {
            invokeTimeProvider = {
                System.currentTimeMillis()
            }
        }
        return invokeTimeProvider
    }

    private boolean shouldRun(long now) {
        def localTimeNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("GMT+8")).toLocalTime()
        def interval = defaultIntervalInMillis

        log.debug("localTimeNow is {}, interval is {}", localTimeNow, interval)

        configList.each { config ->
            if (config.contains(localTimeNow)) {
                interval = config.intervalInMills

                log.debug("config: {} hit on localTimeNow", config)
            }
        }

        def shouldRun = now - lastRanInMillis > interval
        log.debug("lastRanInMillis is {}, shouldRun: {}", lastRanInMillis, shouldRun)

        return shouldRun
    }

    @Override
    void run() {
        log.debug("run method of IntervalAwareRunableWapper is called")
        def now = getInvokeTimeProvider().call()
        log.debug("currentTime is {}", now)

        if (shouldRun(now)) {
            try {
                command.run()
            } finally {
                lastRanInMillis = now
            }
        }
    }

}
