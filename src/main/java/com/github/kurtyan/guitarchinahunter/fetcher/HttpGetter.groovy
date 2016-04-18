package com.github.kurtyan.guitarchinahunter.fetcher

/**
 * Created by yanke on 2016/4/19.
 */
class HttpGetter {

    def setGetMethodCallback = { HttpURLConnection conn ->
        conn.setRequestMethod("GET")
    }
    def userAgentCallback = { HttpURLConnection conn ->
        conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"
        )
    }

    def connectionCallback = [
            setGetMethodCallback,
            userAgentCallback
    ]

    def HttpGetter withConnectionCallback(Closure closure) {
        connectionCallback.add(closure)
        return this
    }

    def String doGet(String url) {
        def conn = url.toURL().openConnection()
        connectionCallback.each { it.call(conn) }

        return conn.inputStream.getText("utf-8")
    }

    def static HttpGetter newInstance() { new HttpGetter() }

}
