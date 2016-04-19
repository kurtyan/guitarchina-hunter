package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.fetcher.HttpGetter
import com.github.kurtyan.guitarchinahunter.parser.ForumPageParser
import org.slf4j.LoggerFactory

/**
 * Created by yanke on 2016/4/19.
 */
class ForumPageCrawler implements Runnable {

    def logger = LoggerFactory.getLogger(getClass())

    def forumPageParser = new ForumPageParser()
    def url = "http://bbs.guitarchina.com/forum-100-1.html"
    def Closure threadEntryHandler

    @Override
    void run() {
        logger.info("begin a new forum page crawl task")
        def urlContent = HttpGetter.newInstance().doGet(url)
        logger.info("got forum page content")

        def threadEntryList = forumPageParser.parse(urlContent)
        threadEntryList.each { entry ->
            try {
                threadEntryHandler.call(entry)
            } catch (Exception e) {
                logger.error("handle thread failed", e)
            }
        }
    }

}
