package com.github.kurtyan.guitarchinahunter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis

/**
 * Created by yanke on 2016/5/5.
 */
class NewThreadFilter(val jedis: Jedis) {

    val log = LoggerFactory.getLogger(javaClass)
    val mapper = ObjectMapper()
    val key = "guitarchina-hunter"

    fun callIfThreadIsNew(entry: ThreadEntry, closure: (ThreadEntry) -> Unit): Unit {
        if (!jedis.hexists(key, entry.id)) {
            val serialized = mapper.writeValueAsString(entry)
            log.info("new thread entry found: {}", entry)

            jedis.hset(key, entry.id, serialized)

            closure.invoke(entry)
        }
    }

}
