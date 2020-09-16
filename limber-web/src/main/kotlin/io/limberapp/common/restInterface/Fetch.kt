package io.limberapp.common.restInterface

import io.limberapp.common.serialization.Json
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import kotlin.browser.window

open class Fetch(private val rootUrl: String, private val json: Json) {
  suspend operator fun invoke(request: LimberEndpoint) = this(request) {}

  suspend operator fun <R> invoke(request: LimberEndpoint, transform: (String) -> R): Outcome<R> {
    val url = request.url
    val headers = headers(request.body != null, accept = request.contentType)
    val requestInit = RequestInit(
      method = request.httpMethod.name,
      headers = headers,
      body = request.body?.let { json.stringify(it) } ?: undefined
    )
    val result = window.fetch(url, requestInit).await()
    @Suppress("MagicNumber")
    return if (result.status in 200..299) Outcome.Success(transform(result.text().await()))
    else Outcome.Failure(FetchFailure(result.status))
  }

  private val LimberEndpoint.url: String get() = rootUrl + href

  protected open suspend fun headers(body: Boolean, accept: ContentType): dynamic {
    return jsObject<dynamic> {
      if (body) this["Content-Type"] = "application/json"
      this["Accept"] = accept.headerValue
    }
  }
}
