package kairo.rest.template

import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal object RestEndpointTemplateParams {
  fun create(endpoint: KClass<out RestEndpoint<*, *>>): List<KParameter> {
    if (endpoint.objectInstance != null) return emptyList()
    val constructor = checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
    return constructor.valueParameters
  }
}
