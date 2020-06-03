package com.piperframework.restInterface

import com.piperframework.serialization.Json
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

open class Fetch(private val rootUrl: String, private val json: Json) {
  suspend operator fun invoke(request: PiperEndpoint) = this(request) {}

  suspend operator fun <R> invoke(request: PiperEndpoint, transform: (String) -> R): Result<R> {
    val url = request.url
    val headers = headers(request.body != null)
    val requestInit = RequestInit(
      method = request.httpMethod.name,
      headers = headers,
      body = request.body?.let { json.stringify(it) } ?: undefined
    )
    val result = window.fetch(url, requestInit).await()
    @Suppress("MagicNumber")
    return if (result.status in 200..299) Result.success(transform(result.text().await()))
    else Result.failure(FetchFailure(result.status))
  }

  private val PiperEndpoint.url: String get() = rootUrl + href

  protected open suspend fun headers(body: Boolean): dynamic {
    return jsObject<dynamic> {
      this["Accept"] = "application/json"
      if (body) this["Content-Type"] = "application/json"
    }
  }
}
