package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import com.github.kurtyan.guitarchinahunter.schedule.IntervalAwareScheduler
import com.github.kurtyan.guitarchinahunter.schedule.IntervalConfigSet
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis

/**
 * Created by yanke on 2016/4/19.
 */

fun main(args: Array<String>) {

    val log = LoggerFactory.getLogger("Main.class")
    val redisHost = System.getenv("redisHost")
    val redisPort = System.getenv("redisPort")
    val smtpServer = System.getenv("smtpServer")
    val smtpUsername = System.getenv("smtpUsername")
    val smtpPasword = System.getenv("smtpPassword")
    val emailSender = System.getenv("emailSender")
    val emailReceiver = System.getenv("emailReceiver")
    val keywordList = System.getenv("keywords").split(",") as List

    log.info("begin hunter execution")

    val threadEntryEmailSender = ThreadEntryEmailSender(
            smtpServer,
            smtpUsername,
            smtpPasword,
            emailSender
    )

    val threadFilter = NewThreadFilter(
            Jedis(redisHost, redisPort as Int)
    )
    val keywordMatcher = KeywordMatcher(keywordList)

    val threadEntryHandler: (ThreadEntry) -> Unit = { entry ->
        threadFilter.callIfThreadIsNew(entry) { newEntry ->
            keywordMatcher.callIfKeywordMatches(newEntry) { matchingKeyword, keywordMatchingEntry ->
                log.info("thread entry matching keyword: {}, entry: {}", matchingKeyword, entry)
                threadEntryEmailSender.send(
                        entry,
                        matchingKeyword,
                        emailReceiver
                )
            }
        }
    }

    val crawler = ForumPageCrawler(threadEntryHandler)

    val intervalAwareScheduler = IntervalAwareScheduler(
            IntervalConfigSet().addIntervalConfig("0100", "0800", 300000L),
            30000L
    )
    intervalAwareScheduler.submit(crawler)

}
