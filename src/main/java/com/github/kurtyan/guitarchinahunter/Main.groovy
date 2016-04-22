package com.github.kurtyan.guitarchinahunter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import com.github.kurtyan.guitarchinahunter.schedule.IntervalAwareScheduler
import com.github.kurtyan.guitarchinahunter.schedule.IntervalConfigSet
import com.github.kurtyan.guitarchinahunter.schedule.RetriableScheduler
import com.guilhermechapiewski.fluentmail.email.EmailMessage
import com.guilhermechapiewski.fluentmail.transport.EmailTransportConfiguration
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

        def keywordList = [
                "horizon",
                "地平线",
                "mh1000",
                "mh-1000",
        ]

        log.info("begin hunter execution")


        def sendEmailExecutor = new RetriableScheduler()
        def mapper = new ObjectMapper()
        def jedis = new Jedis(redisHost, redisPort as int)
        EmailTransportConfiguration.configure(smtpServer, true, true, smtpUsername, smtpPasword);
        def key = "guitarchina-hunter"
        def crawler = new ForumPageCrawler(
                threadEntryHandler: { ThreadEntry entry ->
                    if(!jedis.hexists(key, entry.id)) {
                        def serialized = mapper.writeValueAsString(entry)
                        log.info("new thread entry found: {}", entry)

                        jedis.hset(key, entry.id, serialized)

                        keywordList.each {
                            if (entry.title.toLowerCase(Locale.CHINESE).contains(it)) {
                                log.info("thread entry matching keyword: {}, entry: {}", it, entry)

                                sendEmailExecutor.submit(5) {
                                    try {
                                        log.info("will begin to send mail")

                                        new EmailMessage()
                                                .from(emailSender)
                                                .to(emailReceiver)
                                                .withSubject("thread mathcing keyword: ${it} found")
                                                .withBody(entry.toString())
                                                .send();

                                        log.info("send email succeeded")
                                    } catch (Exception e) {
                                        log.error("send email failed", e)
                                        throw e
                                    }
                                }
                            }
                        }
                    }
                }
        )

        def intervalAwareScheduler = new IntervalAwareScheduler(
                IntervalConfigSet.newInstance().addIntervalConfig("0100", "0800", 300000L),
                30000L
        )
        intervalAwareScheduler.submit(crawler)
    }

}
