package kairo.rest.template

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.rest.RestEndpoint
import kairo.rest.RestEndpointErrorBuilder
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
    private val error: RestEndpointErrorBuilder = RestEndpointErrorBuilder

    public fun from(kClass: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      logger.debug { "Building REST endpoint template (endpoint=$kClass)." }
      require(kClass.isData) { "${error.endpoint(kClass)}: Must be a data class or data object." }
      val params = deriveParams(kClass)
      val template = RestEndpointTemplate(
        method = RestEndpointTemplateMethodParser.parse(kClass),
        path = RestEndpointTemplatePathParser.parse(kClass, params),
        query = RestEndpointTemplateQueryParser.parse(kClass, params),
        contentType = RestEndpointTemplateContentTypeParser.parse(kClass),
        accept = RestEndpointTemplateAcceptParser.parse(kClass),
      )
      logger.debug { "Built REST endpoint template (endpoint=$kClass, template=$template)." }
      return template
    }

    private fun deriveParams(kClass: KClass<out RestEndpoint<*, *>>): List<KParameter> {
      val params =
        if (kClass.objectInstance != null) {
          emptyList()
        } else {
          checkNotNull(kClass.primaryConstructor) { "Data classes always have primary constructors." }
            .let { it.valueParameters }
        }
      params.forEach { param ->
        val paramName = checkNotNull(param.name)
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (paramName == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "${error.endpoint(kClass)}: Body cannot be param."
          }
          return@forEach
        }
        require(!(isPath && isQuery)) {
          "${error.endpoint(kClass)}: Param cannot be both path and query (param=$paramName)."
        }
        require(isPath || isQuery) {
          "${error.endpoint(kClass)}: Param must be path or query (param=$paramName)."
        }
      }
      return params
    }
  }
}

public fun RestEndpointTemplate.toKtorPath(): String =
  path.components.joinToString("/", prefix = "/") { component ->
    when (component) {
      is Component.Constant -> component.value
      is Component.Param -> "{${component.value}}"
    }
  }
