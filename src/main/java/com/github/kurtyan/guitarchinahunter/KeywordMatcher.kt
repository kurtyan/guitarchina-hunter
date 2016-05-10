package com.github.kurtyan.guitarchinahunter

import com.github.kurtyan.guitarchinahunter.parser.entity.ThreadEntry
import java.util.*

/**
 * Created by yanke on 2016/5/5.
 */
class KeywordMatcher(val keywordList: List<String>) {

    fun callIfKeywordMatches(entry: ThreadEntry, closure: (String, ThreadEntry) -> Unit): Unit {
        val matchingKeyword = keywordList.firstOrNull {
            entry.title.toLowerCase(Locale.CHINESE).contains(it)
        }
        if (matchingKeyword != null) {
            closure.invoke(matchingKeyword, entry)
        }
    }

}
