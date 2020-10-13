package io.limberapp.client

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

class LimberHttpClientRequestBuilder(accept: ContentType) {
  val headers
    get() = mutableHeaders
        .mapNotNull { header -> header.value?.let { Pair(header.key, it.toString()) } }
        .associate { it }

  private val mutableHeaders = mutableMapOf<String, Any?>(HttpHeaders.Accept to accept)

  fun header(key: String, value: Any?) {
    mutableHeaders[key] = value
  }
}
