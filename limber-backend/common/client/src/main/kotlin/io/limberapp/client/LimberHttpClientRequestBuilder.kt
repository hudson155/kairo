package io.limberapp.client

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

class LimberHttpClientRequestBuilder(accept: ContentType) {
  val headers
    get() = mutableHeaders
        .mapNotNull { header ->
          // If the [header.value] (which can be anything) is a [Function0] (function that takes no arguments),
          // it will be invoked and its response will be used as the header value.
          // Otherwise, the [header.value] itself will be used.
          val value = when (val value = header.value) {
            null -> null
            is Function0<*> -> value()?.toString()
            else -> value.toString()
          }
          return@mapNotNull value?.let { Pair(header.key, it) }
        }
        .associate { it }

  private val mutableHeaders = mutableMapOf<String, Any?>(HttpHeaders.Accept to accept)

  fun header(key: String, value: Any?) {
    mutableHeaders[key] = value
  }
}
