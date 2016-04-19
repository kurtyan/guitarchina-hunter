package com.github.kurtyan.guitarchinahunter.schedule

import org.junit.Assert
import org.junit.Test

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * Created by yanke on 2016/4/19.
 */
public class IntervalAwareRunableWapperTest {

    @Test
    public void testNotRunable() {
        def runned = Boolean.FALSE
        def toBeWrapped = {
            runned = Boolean.TRUE
        }

        def start = LocalTime.of(3, 0)
        def end = LocalTime.of(3, 10)

        def _0305 = LocalTime.of(3, 5)
        def _030501 = LocalTime.of(3, 5, 1)
        def _030520 = LocalTime.of(3, 5, 20)

        def providedCurrentTime
        def currentTimeProvider = { providedCurrentTime as Long}

        def defaultInterval = 10000L
        def wrapper = new IntervalAwareRunableWapper(
                configList: [
                        new IntervalConfig(
                                startInclusive: start,
                                endExclusive: end,
                                intervalInMills: defaultInterval
                        )
                ],
                invokeTimeProvider: currentTimeProvider,
                command: toBeWrapped
        )

        def providedTimeFormatter = { LocalTime localTime ->
            LocalDateTime.of(LocalDate.now(), localTime).atZone(ZoneId.of("GMT+8")).toInstant().toEpochMilli()
        }

        runned = Boolean.FALSE
        providedCurrentTime = providedTimeFormatter.call(_0305)
        wrapper.run()
        Assert.assertTrue(runned)

        runned = Boolean.FALSE
        providedCurrentTime = providedTimeFormatter.call(_030501)
        wrapper.run()
        Assert.assertFalse(runned)

        runned = Boolean.FALSE
        providedCurrentTime = providedTimeFormatter.call(_030520)
        wrapper.run()
        Assert.assertTrue(runned)
    }

}

