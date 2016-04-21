package com.github.kurtyan.guitarchinahunter.schedule

import org.junit.Test

import java.util.concurrent.Future

/**
 * Created by yanke on 2016/4/22.
 */
class RetriableSchedulerTest extends GroovyTestCase {

    @Test
    void testSubmit() {
        RetriableScheduler scheduler = new RetriableScheduler()

        int ranTimes = 0;
        def future = scheduler.submit(3) {
            ranTimes = ranTimes + 1
            throw new Exception()
        }

        while (future.get() instanceof Future) {
            future = future.get()
        }

        org.junit.Assert.assertEquals(3, ranTimes)
    }

}
