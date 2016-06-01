package com.github.kurtyan.guitarchinahunter.schedule;

import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalAwareRunableWapper(val configList: List<IntervalConfig>,  val defaultIntervalInMillis: Long, val command: Runnable) : Runnable {

    val log = LoggerFactory.getLogger(javaClass)
    var lastRanInMillis = 0L;
    var invokeTimeProvider: () -> Long = {
        System.currentTimeMillis()
    }

    private fun shouldRun(now: Long): Boolean {
        val localTimeNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("GMT+8")).toLocalTime()
        val interval = configList.firstOrNull { it.contains(localTimeNow) }?.intervalInMills ?: defaultIntervalInMillis
        val shouldRun = now - lastRanInMillis > interval;

        log.debug("localTimeNow is {}, interval is {}", localTimeNow, interval)
        log.debug("lastRan is {}, shouldRun: {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(lastRanInMillis), ZoneId.of("GMT+8")), shouldRun)

        return shouldRun
    }

    public override fun run(): Unit {
        val now = invokeTimeProvider.invoke()

        if (this.shouldRun(now)) {
            try {
                command.run()
            } finally {
                lastRanInMillis = now
            }
        }
    }

}
