package com.github.kurtyan.guitarchinahunter.schedule

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalConfigSet {

    val configList = arrayListOf<IntervalConfig>()
    val patterns = listOf<String>(
            "HHmm",
            "HHmmss",
            "HH:mm",
            "HH:mm:ss"
    ).map { DateTimeFormatter.ofPattern(it) }


    fun parse(time: String): LocalTime {
        for (pt in patterns) {
            try {
                return LocalTime.parse(time, pt)
            } catch (e: Exception) {}
        }

        throw IllegalArgumentException("unformatable time: ${time}")
    }

    fun addIntervalConfig(start: String, end: String, interval: Long): IntervalConfigSet {
        configList.add(IntervalConfig(
                parse(start),
                parse(end),
                interval
        ))

        return this
    }

}
