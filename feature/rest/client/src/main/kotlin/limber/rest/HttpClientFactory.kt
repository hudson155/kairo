package limber.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.PrivateBinder
import com.google.inject.PrivateModule
import com.google.inject.name.Named
import com.google.inject.name.Names
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter

private const val BASE_URL: String = "BASE_URL"

public class HttpClientFactory(private val baseUrl: String) : PrivateModule() {
  public class Provider @Inject constructor(
    @Named(BASE_URL) private val baseUrl: String,
    private val objectMapper: ObjectMapper,
  ) : com.google.inject.Provider<HttpClient> {
    override fun get(): HttpClient =
      HttpClient(CIO) {
        install(ContentNegotiation) {
          register(contentType = ContentType.Application.Json, converter = JacksonConverter(objectMapper))
        }
        install(HttpTimeout) {
          requestTimeoutMillis = 15_000
        }
        defaultRequest {
          url(baseUrl)
        }
      }
  }

  override fun configure() {
    bind(String::class.java).annotatedWith(Names.named(BASE_URL)).toInstance(baseUrl)

    bind(HttpClient::class.java).toProvider(Provider::class.java).asEagerSingleton()
    expose(HttpClient::class.java)
  }
}

/**
 * [HttpClient]s are bound by [PrivateBinder.install]ing a child [PrivateModule]
 * so that the base URL can be injected into the [HttpClient] [com.google.inject.Provider]
 * without being [Named] uniquely.
 */
public fun PrivateBinder.bindHttpClient(baseUrl: String) {
  install(HttpClientFactory(baseUrl))
}
