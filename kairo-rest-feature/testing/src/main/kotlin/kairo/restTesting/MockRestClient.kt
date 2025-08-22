package kairo.restTesting

import com.google.inject.Inject
import com.google.inject.Singleton
import io.mockk.mockk
import kairo.rest.client.RestClient
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.handler.RestHandler
import kairo.rest.handler.RestHandlerRegistry

@Singleton
public open class MockRestClient @Inject constructor(
  registry: RestHandlerRegistry,
) : RestClient(registry) {
  public val mock: RestClient = mockk()

  override suspend fun <H : RestHandler<E, O>, E : RestEndpoint<I, O>, I : Any, O> request(endpoint: E): O {
    if (endpoint::class in registry) return super.request(endpoint)
    return mock.request(endpoint)
  }
}
