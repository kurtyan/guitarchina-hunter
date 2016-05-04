package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import com.github.kurtyan.guitarchinahunter.schedule.IntervalAwareScheduler
import com.github.kurtyan.guitarchinahunter.schedule.IntervalConfigSet
import groovy.util.logging.Slf4j
import redis.clients.jedis.Jedis

/**
 * Created by yanke on 2016/4/19.
 */
@Slf4j
class Main {

    public static void main(String[] args) {
        def redisHost = System.getenv("redisHost")
        def redisPort = System.getenv("redisPort")
        def smtpServer = System.getenv("smtpServer")
        def smtpUsername = System.getenv("smtpUsername")
        def smtpPasword = System.getenv("smtpPassword")
        def emailSender = System.getenv("emailSender")
        def emailReceiver = System.getenv("emailReceiver")
        def keywordList = System.getenv("keywords").split(",") as List

        log.info("begin hunter execution")

        def threadEntryEmailSender = new ThreadEntryEmailSender(
                smtpServer,
                smtpUsername,
                smtpPasword,
                emailSender
        )
        def threadFilter = new NewThreadFilter(
                new Jedis(redisHost, redisPort as int)
        )

        def keywordMatcher = new KeywordMatcher(keywordList: keywordList)

        def threadEntryHandler = { ThreadEntry entry ->
            threadFilter.callIfThreadIsNew(entry) { ThreadEntry newEntry ->
                keywordMatcher.callIfKeywordMatches(newEntry) { ThreadEntry keywordMatchingEntry ->
                    log.info("thread entry matching keyword: {}, entry: {}", matchingKeyword, entry)
                    threadEntryEmailSender.send(
                            entry,
                            matchingKeyword,
                            emailReceiver
                    )
                }
            }
        }

        def crawler = new ForumPageCrawler(
                threadEntryHandler: threadEntryHandler
        )

        def intervalAwareScheduler = new IntervalAwareScheduler(
                IntervalConfigSet.newInstance().addIntervalConfig("0100", "0800", 300000L),
                30000L
        )
        intervalAwareScheduler.submit(crawler)
    }

}
