package com.github.kurtyan.guitarchinahunter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import groovy.util.logging.Slf4j
import redis.clients.jedis.Jedis

/**
 * Created by yanke on 2016/5/5.
 */
@Slf4j
class NewThreadFilter {

    def Jedis jedis
    def mapper = new ObjectMapper()
    def key = "guitarchina-hunter"

    NewThreadFilter(Jedis jedis) {
        this.jedis = jedis
    }

    def callIfThreadIsNew(ThreadEntry entry, Closure closure) {
        if (!jedis.hexists(key, entry.id)) {
            def serialized = mapper.writeValueAsString(entry)
            log.info("new thread entry found: {}", entry)

            jedis.hset(key, entry.id, serialized)

            closure.call(entry)
        }
    }

}
