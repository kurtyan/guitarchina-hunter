package com.github.kurtyan.guitarchinahunter.fetcher

import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by yanke on 2016/4/19.
 */
class HttpGetter {


    val setGetMethodCallback: (HttpURLConnection) -> Unit = { it.setRequestMethod("GET") }
    val userAgentCallback: (HttpURLConnection) -> Unit = {
        it.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"
        )
    }

    val connectionCallback = arrayListOf(
            setGetMethodCallback,
            userAgentCallback
    )

    fun withConnectionCallback(callback: (HttpURLConnection) -> Unit):HttpGetter  {
        connectionCallback.add(callback)
        return this
    }

    fun doGet(url: String): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        connectionCallback.forEach { it.invoke(conn) }

        return conn.inputStream.bufferedReader(Charset.forName("utf-8")).readText()
    }

}
