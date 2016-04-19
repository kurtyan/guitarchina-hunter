package com.github.kurtyan.guitarchinahunter.schedule

import groovy.transform.ToString

import java.time.LocalTime

/**
 * Created by yanke on 2016/4/19.
 */
@ToString
class IntervalConfig {

    def LocalTime startInclusive
    def LocalTime endExclusive
    def long intervalInMills

    def boolean contains(LocalTime localTime) {
        return !startInclusive.isAfter(localTime) && endExclusive.isAfter(localTime)
    }

}
