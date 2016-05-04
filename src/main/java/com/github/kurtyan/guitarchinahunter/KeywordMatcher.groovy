package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry

/**
 * Created by yanke on 2016/5/5.
 */
class KeywordMatcher {

    def List<String> keywordList

    def callIfKeywordMatches(ThreadEntry entry, Closure closure) {
        def matchingKeyword = null
        keywordList.each { keyword ->
            if (entry.title.toLowerCase(Locale.CHINESE).contains(keyword)) {
                matchingKeyword = keyword
            }

        }

        if (matchingKeyword != null) {
            closure.call(entry)
        }
    }

}
