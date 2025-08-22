package kairo.rest.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kairo.rest.KtorServerMapper
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.handler.RestHandler
import kairo.rest.handler.RestHandlerRegistry
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.typeReference

/**
 * Provides access to all [RestHandler]s.
 *
 * When injecting this in production code, always inject a [Provider].
 */
@Singleton
public open class RestClient @Inject constructor(
  protected val registry: RestHandlerRegistry,
) {
  @Suppress("ForbiddenMethodCall", "UNCHECKED_CAST")
  public open suspend fun <H : RestHandler<E, O>, E : RestEndpoint<I, O>, I : Any, O : Any?> request(endpoint: E): O {
    val handler = checkNotNull(registry[endpoint::class]) {
      "REST handler registry had no entry for ${endpoint::class.qualifiedName!!}."
    } as H
    /**
     * We call [ObjectMapper.convertValue] to put the [E] instance through (de)serialization.
     * Without doing this, Jackson transformations such as [TrimWhitespace] wouldn't get called.
     */
    return handler.handle(KtorServerMapper.json.convertValue(endpoint, handler.endpointType.typeReference))
  }
}
