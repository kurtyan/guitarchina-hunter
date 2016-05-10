package com.github.kurtyan.guitarchinahunter.schedule;

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

/**
 * Created by yanke on 2016/4/20.
 */
class IntervalConfigSetTest {

    @Test
    fun parse(): Unit  {
        val set = IntervalConfigSet()
        val time = LocalTime.of(0, 0, 0);

        mapOf(
                Pair("0000" , time),
                Pair("000000" , time),
                Pair("00:00" , time),
                Pair("00:00:00" , time)
        ).forEach {
            assertEquals(
                    it.value,
                    set.parse(it.key)
            )
        }
    }

}