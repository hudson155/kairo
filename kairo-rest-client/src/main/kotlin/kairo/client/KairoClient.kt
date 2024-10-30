@file:Suppress("ForbiddenImport")

package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter

public typealias KairoClient = HttpClient

public fun createKairoClient(
  block: HttpClientConfig<CIOEngineConfig>.() -> Unit = {},
): KairoClient =
  HttpClient(CIO) {
    install(ContentNegotiation) {
      register(
        contentType = ContentType.Application.Json,
        converter = JacksonConverter(objectMapper = ktorClientMapper, streamRequestBody = false),
      )
    }
    block()
  }
