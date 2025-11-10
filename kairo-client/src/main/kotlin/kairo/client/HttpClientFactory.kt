package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kairo.optional.optionalModule
import kotlin.time.Duration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.plus

/**
 * Creates Ktor HTTP clients.
 */
public object HttpClientFactory {
  public fun create(
    timeout: Duration,
    configureJson: JsonBuilder.() -> Unit,
    block: HttpClientConfig<*>.() -> Unit,
  ): HttpClient =
    HttpClient(Java) {
      expectSuccess = true
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            serializersModule += optionalModule()
            configureJson()
          },
        )
      }
      install(HttpTimeout) {
        requestTimeoutMillis = timeout.inWholeMilliseconds
      }
      block()
    }
}
