package com.github.kurtyan.guitarchinahunter.parser

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by yanke on 2016/3/28.
 */
class ForumPageParser {

    def List<ThreadEntry> parse(String forumPage) {
        def document = Jsoup.parse(forumPage)
        def threadElementList = document.getElementsByAttributeValueStarting("id", "normalthread")

        threadElementList.collect { Element element ->
            def threadTitleElement = element.getElementsByAttributeValueContaining("href", "thread")[1]
            def threadPath = threadTitleElement.attr("href")

            return new ThreadEntry(
                    id: threadPath.replaceAll("thread-", "").replaceAll("-1-1.html", ""),
                    title: threadTitleElement.text(),
                    url: "http://bbs.guitarchina.com/${threadPath}"
            )
        }
    }

}
