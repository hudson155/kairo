package kairo.rest

import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal class RestEndpointTemplateParams(params: List<KParameter>) : List<KParameter> by params {
  internal companion object {
    context(routing: KairoRouting<*>)
    fun create(): RestEndpointTemplateParams {
      val params = params()
      return RestEndpointTemplateParams(params)
    }

    context(routing: KairoRouting<*>)
    private fun params(): List<KParameter> {
      val endpoint = routing.endpoint.kotlinClass
      if (endpoint.objectInstance != null) return emptyList()
      val constructor = checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
      return constructor.valueParameters
    }
  }
}
