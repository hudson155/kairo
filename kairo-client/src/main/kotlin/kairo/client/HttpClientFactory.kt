package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import kairo.serialization.KairoJson
import kotlin.time.Duration

/**
 * Creates Ktor HTTP clients.
 */
public object HttpClientFactory {
  @OptIn(KairoJson.RawJsonMapper::class)
  public fun create(
    timeout: Duration,
    json: KairoJson,
    block: HttpClientConfig<*>.() -> Unit,
  ): HttpClient =
    HttpClient(Java) {
      expectSuccess = true
      install(ContentNegotiation) {
        register(
          contentType = ContentType.Application.Json,
          converter = JacksonConverter(json.delegate),
        )
      }
      install(HttpTimeout) {
        requestTimeoutMillis = timeout.inWholeMilliseconds
      }
      block()
    }
}
