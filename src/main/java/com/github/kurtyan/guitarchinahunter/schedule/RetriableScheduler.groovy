package com.github.kurtyan.guitarchinahunter.schedule

import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by yanke on 2016/4/22.
 */
class RetriableScheduler {

    def executor = Executors.newCachedThreadPool()

    def Future submit(int maxRetryTimes, Runnable runnable) {
        return executor.submit {
            for (int i = 0; i < maxRetryTimes; i++) {
                try {
                    runnable.run()
                    return
                } catch (Exception e) {}
            }
        }
    }

}
