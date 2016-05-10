package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.fetcher.HttpGetter
import com.github.kurtyan.guitarchinahunter.parser.ForumPageParser
import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import org.slf4j.LoggerFactory

/**
 * Created by yanke on 2016/4/19.
 */
class ForumPageCrawler(val threadEntryHandler: (ThreadEntry) -> Unit) : Runnable {

    val logger = LoggerFactory.getLogger(javaClass)
    val forumPageParser = ForumPageParser()
    val url = "http://bbs.guitarchina.com/forum-100-1.html"

    override fun run() {
        logger.info("begin a new forum page crawl task")
        val urlContent = HttpGetter().doGet(url)
        logger.info("got forum page content")

        forumPageParser.parse(urlContent).forEach { entry ->
            try {
                threadEntryHandler.invoke(entry)
            } catch (e: Exception) {
                logger.error("handle thread failed", e)
            }
        }
    }

}
