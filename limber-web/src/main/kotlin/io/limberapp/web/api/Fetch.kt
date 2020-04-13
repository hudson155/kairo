package io.limberapp.web.api

import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import io.limberapp.web.util.encodeURIComponent
import io.limberapp.web.util.process
import kotlinext.js.JsObject
import kotlinext.js.asJsObject
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

private enum class HttpMethod { DELETE, GET, PATCH, POST, PUT }

internal object Api {

    suspend fun delete(path: String) = fetch(
        httpMethod = HttpMethod.DELETE,
        path = path,
        queryParams = emptyList(),
        body = null
    )

    suspend fun get(path: String, queryParams: List<Pair<String, String>> = emptyList()) = fetch(
        httpMethod = HttpMethod.GET,
        path = path,
        queryParams = queryParams,
        body = null
    )

    suspend fun patch(path: String, body: UpdateRep) = fetch(
        httpMethod = HttpMethod.PATCH,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    suspend fun post(path: String, body: CreationRep) = fetch(
        httpMethod = HttpMethod.POST,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    suspend fun put(path: String, body: CreationRep?) = fetch(
        httpMethod = HttpMethod.PUT,
        path = path,
        queryParams = emptyList(),
        body = body
    )

    private suspend fun fetch(
        httpMethod: HttpMethod,
        path: String,
        queryParams: List<Pair<String, String>>,
        body: Any?
    ): Any? {
        val url = url(path, queryParams)
        val headers = headers(body != null)
        val requestInit = RequestInit(
            method = httpMethod.name,
            headers = headers,
            body = body?.let { JSON.stringify(it) } ?: undefined
        )
        return window.fetch(url, requestInit).await().json().await()
    }

    private fun url(path: String, queryParams: List<Pair<String, String>>): String {
        val queryString = queryParams.joinToString("&") {
            "${encodeURIComponent(it.first)}=${encodeURIComponent(it.second)}"
        }
        var url = process.env.API_ROOT_URL + path.split('/').joinToString("/") { encodeURIComponent(it) }
        if (queryString.isNotEmpty()) url += "?$queryString"
        return url
    }

    private fun headers(body: Boolean): dynamic {
        val headers = jsObject<dynamic> {
            this["Accept"] = "application/json"
        }
        if (body) headers["Content-Type"] = "application/json"
        return headers
    }
}
