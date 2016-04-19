package com.github.kurtyan.guitarchinahunter.schedule

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import java.time.LocalTime

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalConfigTest {

    def config
    def now = LocalTime.now()
    def _10minsLater = now.plusMinutes(10L)
    def _20minsLater = now.plusMinutes(20L)
    def _30minsLater = now.plusMinutes(30L)

    @Before
    public void setUp() throws Exception {
        config = new IntervalConfig(
                startInclusive: now,
                endExclusive: _20minsLater,
                intervalInMills: 1L
        )
    }

    @org.junit.Test
    public void testContains() throws Exception {
        Assert.assertTrue(
                config.contains(_10minsLater)
        )
    }

    @Test
    def void testContainsStart() {
        Assert.assertTrue(
                config.contains(now)
        )
    }

    @Test
    def void testNotContainsEnd() {
        Assert.assertFalse(
                config.contains(_20minsLater)
        )
    }

    @Test
    def void testNotContains() {
        Assert.assertFalse(
                config.contains(_30minsLater)
        )
    }

}
