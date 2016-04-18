package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.fetcher.HttpGetter
import com.github.kurtyan.guitarchinahunter.parser.ForumPageParser

/**
 * Created by yanke on 2016/4/19.
 */
class ForumPageCrawler implements Runnable {

    def forumPageParser = new ForumPageParser()
    def url = "http://bbs.guitarchina.com/forum-100-1.html"
    def Closure threadEntryHandler

    @Override
    void run() {
        def urlContent = HttpGetter.newInstance().doGet(url)
        def threadEntryList = forumPageParser.parse(urlContent)
        threadEntryList.each { entry ->
            threadEntryHandler.call(entry)
        }
    }

}
