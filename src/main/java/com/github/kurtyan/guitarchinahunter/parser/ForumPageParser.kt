package com.github.kurtyan.guitarchinahunter.parser

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import org.jsoup.Jsoup

/**
 * Created by yanke on 2016/3/28.
 */
class ForumPageParser {

    fun parse(forumPage: String): List<ThreadEntry> {
        val document = Jsoup.parse(forumPage)
        val threadElementList = document.getElementsByAttributeValueStarting("id", "normalthread")

        val mapped = threadElementList.map { element ->
            val threadTitleElement = element.getElementsByAttributeValueContaining("href", "thread")[1]
            val threadPath = threadTitleElement.attr("href")

            val entry = ThreadEntry(
                    threadPath.replace("thread-", "").replace("-1-1.html", ""),
                    threadTitleElement.text(),
                    "http://bbs.guitarchina.com/${threadPath}"
            )

            return entry
        }


        return mapped
    }

}
