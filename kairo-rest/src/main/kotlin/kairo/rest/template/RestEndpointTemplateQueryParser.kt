package kairo.rest.template

import kairo.rest.RestEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

internal object RestEndpointTemplateQueryParser {
  private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

  fun parse(
    endpoint: KClass<out RestEndpoint<*, *>>,
    params: List<KParameter>,
  ): RestEndpointTemplateQuery {
    val queryParams = params.filter { it.hasAnnotation<RestEndpoint.QueryParam>() }
    validateQueryParams(endpoint, queryParams)
    return RestEndpointTemplateQuery(
      queryParams.map { param ->
        RestEndpointTemplateQuery.Param(
          value = checkNotNull(param.name),
          required = !param.type.isMarkedNullable,
        )
      },
    )
  }

  private fun validateQueryParams(
    endpoint: KClass<out RestEndpoint<*, *>>,
    queryParams: List<KParameter>,
  ) {
    queryParams.forEach { param ->
      require(!param.isOptional) {
        "${error.endpoint(endpoint)}: ${error.queryParamAnnotation} must not be optional (param=${param.name})."
      }
    }
  }
}
