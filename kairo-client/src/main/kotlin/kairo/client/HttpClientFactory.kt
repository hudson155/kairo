package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kairo.ktor.kairoConversion
import kairo.serialization.KairoJson
import kotlin.time.Duration

/**
 * Creates Ktor HTTP clients.
 */
public object HttpClientFactory {
  public fun create(
    timeout: Duration,
    json: KairoJson,
    block: HttpClientConfig<*>.() -> Unit,
  ): HttpClient =
    HttpClient(Java) {
      expectSuccess = true
      install(ContentNegotiation) {
        kairoConversion(json)
      }
      install(HttpTimeout) {
        requestTimeoutMillis = timeout.inWholeMilliseconds
      }
      block()
    }
}
