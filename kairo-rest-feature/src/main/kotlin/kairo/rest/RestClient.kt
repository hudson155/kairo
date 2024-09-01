package kairo.rest

import com.google.inject.Inject

public class RestClient @Inject constructor(
  private val registry: RestHandlerRegistry,
) {
  public suspend fun <H : RestHandler<E, O>, E : RestEndpoint<I, O>, I : Any, O : Any?> request(endpoint: E): O {
    @Suppress("UNCHECKED_CAST")
    val handler = checkNotNull(registry[endpoint::class]) {
      "REST handler registry had no entry for ${endpoint::class.simpleName!!}."
    } as H
    return handler.handle(endpoint)
  }
}
