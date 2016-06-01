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
class IntervalAwareRunableWapperTest {

    @Test
    fun testNotRunable(): Unit {
        var runned = false
        val toBeWrapped = Runnable {
            runned = true
        }

        val start = LocalTime.of(3, 0)
        val end = LocalTime.of(3, 10)

        val _0305 = LocalTime.of(3, 5)
        val _030501 = LocalTime.of(3, 5, 1)
        val _030520 = LocalTime.of(3, 5, 20)

        var providedCurrentTime = 0L
        val currentTimeProvider: () -> Long = { providedCurrentTime }

        val defaultInterval = 10000L
        val wrapper = IntervalAwareRunableWapper(
                listOf( IntervalConfig(start, end, defaultInterval) ),
                1L,
                toBeWrapped
        )
        wrapper.invokeTimeProvider = currentTimeProvider

        val providedTimeFormatter: (LocalTime) -> Long = { localTime ->
            LocalDateTime.of(LocalDate.now(), localTime).atZone(ZoneId.of("GMT+8")).toInstant().toEpochMilli()
        }

        runned = false
        providedCurrentTime = providedTimeFormatter.invoke(_0305)
        wrapper.run()
        Assert.assertTrue(runned)

        runned = false
        providedCurrentTime = providedTimeFormatter.invoke(_030501)
        wrapper.run()
        Assert.assertFalse(runned)

        runned = false
        providedCurrentTime = providedTimeFormatter.invoke(_030520)
        wrapper.run()
        Assert.assertTrue(runned)
    }

}

