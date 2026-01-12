package kairo.rest.template

import kairo.rest.RestEndpoint
import kairo.rest.RestEndpointErrorBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

internal object RestEndpointTemplateQueryParser {
  private val error: RestEndpointErrorBuilder = RestEndpointErrorBuilder

  fun parse(
    kClass: KClass<out RestEndpoint<*, *>>,
    params: List<KParameter>,
  ): RestEndpointTemplateQuery {
    val queryParams = params.filter { it.hasAnnotation<RestEndpoint.QueryParam>() }
    validateQueryParams(kClass, queryParams)
    return RestEndpointTemplateQuery(
      queryParams.map { param ->
        val paramName = checkNotNull(param.name)
        RestEndpointTemplateQuery.Param(
          value = paramName,
          required = !param.type.isMarkedNullable,
        )
      },
    )
  }

  private fun validateQueryParams(
    kClass: KClass<out RestEndpoint<*, *>>,
    queryParams: List<KParameter>,
  ) {
    queryParams.forEach { param ->
      val paramName = checkNotNull(param.name)
      require(!param.isOptional) {
        "${error.endpoint(kClass)}: ${error.queryParamAnnotation} must not be optional (param=$paramName)."
      }
    }
  }
}
