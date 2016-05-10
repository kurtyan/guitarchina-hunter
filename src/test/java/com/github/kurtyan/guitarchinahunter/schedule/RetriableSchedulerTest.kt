package com.github.kurtyan.guitarchinahunter.schedule

import org.junit.Test

/**
 * Created by yanke on 2016/4/22.
 */
class  RetriableSchedulerTest {

    @Test
    fun  testSubmit(): Unit {
        val scheduler = RetriableScheduler()

        var ranTimes: Int = 0
        val future = scheduler.submit(3, Runnable() {
            ranTimes = ranTimes + 1
            throw Exception()
        })

        future.get()
        org.junit.Assert.assertEquals(3, ranTimes)
    }

}
