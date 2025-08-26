package kairo.rest.template

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplatePath.Component
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A REST endpoint template instance represents a specific subclass of [RestEndpoint].
 * In fact, it can be created from a [RestEndpoint] class reference using [RestEndpointTemplate.from].
 */
public data class RestEndpointTemplate(
  val method: HttpMethod,
  val path: RestEndpointTemplatePath,
  val query: RestEndpointTemplateQuery,
  val contentType: ContentType?,
  val accept: ContentType?,
) {
  override fun toString(): String {
    val value = buildList {
      add("[$contentType -> $accept]")
      add(method.toString())
      add(path.toString())
      if (query.params.isNotEmpty()) add(query.toString())
    }.joinToString(" ")
    return "RestEndpointTemplate(value='$value')"
  }

  public companion object {
    private val error: RestEndpointTemplateErrorBuilder = RestEndpointTemplateErrorBuilder

    public fun from(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      logger.debug { "Building REST endpoint template (endpoint=$endpoint)." }
      require(endpoint.isData) { "${error.endpoint(endpoint)}: Must be a data class or data object." }
      val params = deriveParams(endpoint)
      val template = RestEndpointTemplate(
        method = RestEndpointTemplateMethodParser.parse(endpoint),
        path = RestEndpointTemplatePathParser.parse(endpoint, params),
        query = RestEndpointTemplateQueryParser.parse(endpoint, params),
        contentType = RestEndpointTemplateContentTypeParser.parse(endpoint),
        accept = RestEndpointTemplateAcceptParser.parse(endpoint),
      )
      logger.debug { "Built REST endpoint template (endpoint=$endpoint, template=$template)." }
      return template
    }

    private fun deriveParams(endpoint: KClass<out RestEndpoint<*, *>>): List<KParameter> {
      val params =
        if (endpoint.objectInstance != null) {
          emptyList()
        } else {
          checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
            .let { it.valueParameters }
        }
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "${error.endpoint(endpoint)}: Body cannot be param."
          }
          return@forEach
        }
        require(!(isPath && isQuery)) {
          "${error.endpoint(endpoint)}: Param cannot be both path and query (param=${param.name})."
        }
        require(isPath || isQuery) {
          "${error.endpoint(endpoint)}: Param must be path or query (param=${param.name})."
        }
      }
      return params
    }
  }
}

internal fun RestEndpointTemplate.toKtorPath(): String =
  path.components.joinToString("/", prefix = "/") { component ->
    when (component) {
      is Component.Constant -> component.value
      is Component.Param -> "{${component.value}}"
    }
  }
