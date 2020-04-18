package io.limberapp.web.api

import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.web.util.encodeURIComponent
import io.limberapp.web.util.process
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

private enum class HttpMethod { DELETE, GET, PATCH, POST, PUT }

internal val json = Json(context = formsSerialModule)

internal open class Fetch {

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

    private fun url(path: String, queryParams: List<Pair<String, String>>): String {
        val queryString = queryParams.joinToString("&") {
            "${encodeURIComponent(it.first)}=${encodeURIComponent(it.second)}"
        }
        var url = process.env.API_ROOT_URL + path.split('/').joinToString("/") { encodeURIComponent(it) }
        if (queryString.isNotEmpty()) url += "?$queryString"
        return url
    }

    protected open suspend fun headers(body: Boolean): dynamic {
        return jsObject<dynamic> {
            this["Accept"] = "application/json"
            if (body) this["Content-Type"] = "application/json"
        }
    }
}
