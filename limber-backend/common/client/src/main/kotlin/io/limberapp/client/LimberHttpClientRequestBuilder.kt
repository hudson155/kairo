package io.limberapp.client

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

typealias RequestBuilder = LimberHttpClientRequestBuilder.() -> Unit

class LimberHttpClientRequestBuilder(accept: ContentType) {
  private val mutableHeaders: MutableMap<String, Any> = mutableMapOf(HttpHeaders.Accept to accept)

  val headers: Map<String, String>
    get() = mutableHeaders
        .mapNotNull { header ->
          val value = when (val value = header.value) {
            is Function0<*> -> value()?.toString()
            else -> value.toString()
          }
          return@mapNotNull value?.let { Pair(header.key, it) }
        }
        .associate { it }

  /**
   * The [value] can be anything. If it's a [Function0] (function that takes no arguments), it will
   * be invoked and its response will be used as the header value. Otherwise, the [value] itself
   * will be used.
   */
  @Suppress("KDocUnresolvedReference")
  fun putHeader(key: String, value: Any) {
    check(!HttpHeaders.isUnsafe(key))
    mutableHeaders[key] = value
  }
}
