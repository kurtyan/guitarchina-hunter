package com.github.kurtyan.guitarchinahunter.schedule

import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by yanke on 2016/4/22.
 */
class RetriableScheduler {

    val executor = Executors.newCachedThreadPool()

    fun submit(maxRetryTimes: Int, runnable: Runnable):Future<*> {
        return executor.submit {
            for (i in 0..maxRetryTimes - 1) {
                try {
                    runnable.run()
                } catch (e: Exception) {}
            }
        }
    }

}
