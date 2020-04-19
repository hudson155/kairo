package com.piperframework.restInterface

import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.util.encodeURIComponent
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

open class Fetch(private val rootUrl: String) {

    @Deprecated("API Transition")
    suspend fun delete(path: String) = fetch(
        httpMethod = HttpMethod.DELETE,
        path = path,
        queryParams = emptyList(),
        body = null
    )

    @Deprecated("API Transition")
    suspend fun get(path: String, queryParams: List<Pair<String, String>> = emptyList()) = fetch(
        httpMethod = HttpMethod.GET,
        path = path,
        queryParams = queryParams,
        body = null
    )

    @Deprecated("API Transition")
    suspend fun patch(path: String, body: UpdateRep) = fetch(
        httpMethod = HttpMethod.PATCH,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    @Deprecated("API Transition")
    suspend fun post(path: String, body: CreationRep) = fetch(
        httpMethod = HttpMethod.POST,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    @Deprecated("API Transition")
    suspend fun put(path: String, body: CreationRep?) = fetch(
        httpMethod = HttpMethod.PUT,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    @Deprecated("API Transition")
    private suspend fun fetch(
        httpMethod: HttpMethod,
        path: String,
        queryParams: List<Pair<String, String>>,
        body: Any?
    ): String {
        val url = url(path, queryParams)
        val headers = headers(body != null)
        val requestInit = RequestInit(
            method = httpMethod.name,
            headers = headers,
            body = body?.let { JSON.stringify(it) } ?: undefined
        )
        return window.fetch(url, requestInit).await().text().await()
    }

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

    @Deprecated("API Transition")
    private fun url(path: String, queryParams: List<Pair<String, String>>): String {
        val queryString = queryParams.joinToString("&") {
            "${encodeURIComponent(it.first)}=${encodeURIComponent(it.second)}"
        }
        var url = rootUrl + path.split('/').joinToString("/") { encodeURIComponent(it) }
        if (queryString.isNotEmpty()) url += "?$queryString"
        return url
    }

    private val PiperEndpoint.url: String get() = rootUrl + href

    protected open suspend fun headers(body: Boolean): dynamic {
        return jsObject<dynamic> {
            this["Accept"] = "application/json"
            if (body) this["Content-Type"] = "application/json"
        }
    }
}
