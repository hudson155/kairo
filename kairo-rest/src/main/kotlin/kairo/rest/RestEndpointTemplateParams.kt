package kairo.rest

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal class RestEndpointTemplateParams(params: List<KParameter>) : List<KParameter> by params {
  internal companion object {
    fun from(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplateParams {
      val params = params(endpoint)
      return RestEndpointTemplateParams(params)
    }

    private fun params(endpoint: KClass<out RestEndpoint<*, *>>): List<KParameter> {
      if (endpoint.objectInstance != null) return emptyList()
      val constructor = checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
      return constructor.valueParameters
    }
  }
}
