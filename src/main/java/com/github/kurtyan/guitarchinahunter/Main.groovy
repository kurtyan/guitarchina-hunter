package com.github.kurtyan.guitarchinahunter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by yanke on 2016/4/19.
 */
class Main {

    def static logger = LoggerFactory.getLogger(getClass())

    public static void main(String[] args) {
        if (args.size() != 2) {
            System.exit(1)
        }

        def keywordList = [
                "horizon",
                "地平线",
                "mh1000",
                "mh-1000",
        ]

        def mapper = new ObjectMapper()
        def jedis = new Jedis(args[0], args[1] as int)
        def key = "guitarchin-hunter"
        def crawler = new ForumPageCrawler(
                threadEntryHandler: { ThreadEntry entry ->
                    if(!jedis.hexists(key, entry.id)) {
                        def serialized = mapper.writeValueAsString(entry)
                        jedis.hset(key, entry.id, serialized)

                        keywordList.each {
                            if (entry.title.toLowerCase().contains(it)) {
                                logger.info(serialized)
                            }
                        }
                    }
                }
        )

        def pool = Executors.newScheduledThreadPool(1)
        pool.scheduleAtFixedRate(
                crawler,
                0,
                10,
                TimeUnit.SECONDS
        )
    }

}
