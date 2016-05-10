package com.github.kurtyan.guitarchinahunter.schedule

import java.time.LocalTime

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalConfig(val startInclusive: LocalTime, val endExclusive: LocalTime, val intervalInMills: Long) {

    fun contains(localTime: LocalTime): Boolean {
        return !startInclusive.isAfter(localTime) && endExclusive.isAfter(localTime)
    }

}
