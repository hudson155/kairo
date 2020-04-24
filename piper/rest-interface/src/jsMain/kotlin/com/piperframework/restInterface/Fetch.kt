package com.piperframework.restInterface

import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

open class Fetch(private val rootUrl: String) {
    suspend operator fun invoke(request: PiperEndpoint): String {
        val url = request.url
        val headers = headers(request.body != null)
        val requestInit = RequestInit(
            method = request.httpMethod.name,
            headers = headers,
            body = request.body?.let { JSON.stringify(it) } ?: undefined
        )
        return window.fetch(url, requestInit).await().text().await()
    }

    private val PiperEndpoint.url: String get() = rootUrl + href

    protected open suspend fun headers(body: Boolean): dynamic {
        return jsObject<dynamic> {
            this["Accept"] = "application/json"
            if (body) this["Content-Type"] = "application/json"
        }
    }
}
