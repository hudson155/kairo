package kairo.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import kairo.serialization.TrimWhitespace

public class RestClient @Inject constructor(
  private val registry: RestHandlerRegistry,
) {
  public suspend fun <H : RestHandler<E, O>, E : RestEndpoint<I, O>, I : Any, O : Any?> request(endpoint: E): O {
    @Suppress("UNCHECKED_CAST")
    val handler = checkNotNull(registry[endpoint::class]) {
      "REST handler registry had no entry for ${endpoint::class.qualifiedName!!}."
    } as H
    /**
     * We call [ObjectMapper.convertValue] to put the [E] instance through (de)serialization.
     * Without doing this, Jackson transformations such as [TrimWhitespace] wouldn't get called.
     */
    @Suppress("ForbiddenMethodCall")
    return handler.handle(ktorMapper.convertValue(endpoint, handler.endpoint.java))
  }
}
