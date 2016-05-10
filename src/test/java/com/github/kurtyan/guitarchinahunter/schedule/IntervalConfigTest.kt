package com.github.kurtyan.guitarchinahunter.schedule

import org.junit.Assert
import org.junit.Test
import java.time.LocalTime

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalConfigTest {

    val now = LocalTime.now()
    val _10minsLater = now.plusMinutes(10L)
    val _20minsLater = now.plusMinutes(20L)
    val _30minsLater = now.plusMinutes(30L)
    val config = IntervalConfig(
            now,
            _20minsLater,
            1L
    )

    @Test
    fun testContains(): Unit {
        Assert.assertTrue(
                config.contains(_10minsLater)
        )
    }

    @Test
    fun testContainsStart(): Unit {
        Assert.assertTrue(
                config.contains(now)
        )
    }

    @Test
    fun testNotContainsEnd(): Unit {
        Assert.assertFalse(
                config.contains(_20minsLater)
        )
    }

    @Test
    fun  testNotContains(): Unit {
        Assert.assertFalse(
                config.contains(_30minsLater)
        )
    }

}
