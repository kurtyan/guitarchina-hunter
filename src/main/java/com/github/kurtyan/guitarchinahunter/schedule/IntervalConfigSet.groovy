package com.github.kurtyan.guitarchinahunter.schedule

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by yanke on 2016/4/19.
 */
class IntervalConfigSet {

    def List<IntervalConfig> configList = [] as List
    def patterns = [
            "HHmm",
            "HHmmss",
            "HH:mm",
            "HH:mm:ss"
    ].collect { DateTimeFormatter.ofPattern(it)}

    def LocalTime parse(String time) {
        for (def pt : patterns) {
            try {
                return LocalTime.parse(time, pt)
            } catch (Exception e) {}
        }

        throw new IllegalArgumentException("unformatable time: ${time}")
    }


    def IntervalConfigSet addIntervalConfig(String start, String end, long interval) {
        configList.add(
                new IntervalConfig(
                        startInclusive: parse(start),
                        endExclusive: parse(end),
                        intervalInMills: interval
                )
        )

        return this
    }

    def static IntervalConfigSet newInstance() { return new IntervalConfigSet() }

}
