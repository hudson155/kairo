package kairo.rest.template

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.rest.RestEndpoint
import kairo.rest.template.RestEndpointTemplatePath.Component
import kairo.rest.template.RestEndpointTemplateQuery.Param
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
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
      val template = build(endpoint)
      logger.debug { "Built REST endpoint template (endpoint=$endpoint, template=$template)." }
      return template
    }

    private fun build(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      require(endpoint.isData) { "${error.endpoint(endpoint)}: Must be a data class or data object." }
      val params = deriveParams(endpoint)
      validateParams(endpoint, params)
      return RestEndpointTemplate(
        method = parseMethod(endpoint),
        path = parsePath(endpoint, params),
        query = parseQuery(endpoint, params),
        contentType = parseContentType(endpoint),
        accept = parseAccept(endpoint),
      )
    }

    private fun deriveParams(endpoint: KClass<out RestEndpoint<*, *>>): List<KParameter> {
      if (endpoint.objectInstance != null) return emptyList()
      val constructor = checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
      return constructor.valueParameters
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [RestEndpoint.Path]
     * are validated within the appropriate parse methods in this class.
     */
    private fun validateParams(
      endpoint: KClass<out RestEndpoint<*, *>>,
      params: List<KParameter>,
    ) {
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "${error.endpoint(endpoint)}: Body cannot be param."
          }
          return@validateParams
        }
        require(!(isPath && isQuery)) {
          "${error.endpoint(endpoint)}: Param cannot be both path and query (param=${param.name})."
        }
        require(isPath || isQuery) {
          "${error.endpoint(endpoint)}: Param must be path or query (param=${param.name})."
        }
      }
    }

    private fun parseMethod(endpoint: KClass<out RestEndpoint<*, *>>): HttpMethod {
      val annotations = endpoint.findAnnotations<RestEndpoint.Method>()
      require(annotations.isNotEmpty()) {
        "${error.endpoint(endpoint)}: Must define ${error.methodAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.endpoint(endpoint)}: Cannot define multiple of ${error.methodAnnotation}."
      }
      return HttpMethod.parse(annotation.value.uppercase())
    }

    private fun parsePath(
      endpoint: KClass<out RestEndpoint<*, *>>,
      params: List<KParameter>,
    ): RestEndpointTemplatePath {
      val pathParams = params.filter { it.hasAnnotation<RestEndpoint.PathParam>() }
      pathParams.forEach { param ->
        require(!param.type.isMarkedNullable) {
          "${error.endpoint(endpoint)}: ${error.pathParamAnnotation} must not be nullable (param=${param.name})."
        }
        require(!param.isOptional) {
          "${error.endpoint(endpoint)}: ${error.pathParamAnnotation} must not be optional (param=${param.name})."
        }
      }
      val annotations = endpoint.findAnnotations<RestEndpoint.Path>()
      require(annotations.isNotEmpty()) {
        "${error.endpoint(endpoint)}: Must define ${error.pathAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.endpoint(endpoint)}: Cannot define multiple of ${error.pathAnnotation}."
      }
      try {
        return RestEndpointTemplatePath.from(annotation.value).also { path ->
          path.components.forEachIndexed { i, component ->
            if (component !is Component.Param) return@forEachIndexed
            require(path.components.take(i).none { it is Component.Param && it.value == component.value }) {
              "Duplicate param (param=${component.value})"
            }
            require(pathParams.any { it.name == component.value }) {
              "Missing ${error.pathParamAnnotation} (param=${component.value})"
            }
          }
          pathParams.forEach { paramName ->
            require(path.components.any { it is Component.Param && it.value == paramName.name }) {
              "Missing ${error.pathParamAnnotation} (param=${paramName.name})"
            }
          }
        }
      } catch (e: IllegalArgumentException) {
        val eMessage = buildString {
          append("${error.endpoint(endpoint)}: ${error.pathAnnotation} is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
    }

    private fun parseQuery(
      endpoint: KClass<out RestEndpoint<*, *>>,
      params: List<KParameter>,
    ): RestEndpointTemplateQuery {
      val queryParams = params.filter { it.hasAnnotation<RestEndpoint.QueryParam>() }
      queryParams.forEach { param ->
        require(!param.isOptional) {
          "${error.endpoint(endpoint)}: ${error.queryParamAnnotation} must not be optional (param=${param.name})."
        }
      }
      return RestEndpointTemplateQuery(
        queryParams.map { Param(value = it.name!!, required = !it.type.isMarkedNullable) },
      )
    }

    private fun parseContentType(endpoint: KClass<out RestEndpoint<*, *>>): ContentType? {
      val annotations = endpoint.findAnnotations<RestEndpoint.ContentType>()
      if (annotations.isEmpty()) return null
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "Endpoint ${endpoint.qualifiedName} cannot define multiple of" +
          " @${RestEndpoint::class.simpleName}.${RestEndpoint.ContentType::class.simpleName}."
      }
      try {
        return ContentType.parse(annotation.value)
      } catch (e: BadContentTypeFormatException) {
        val eMessage = buildString {
          append("${error.endpoint(endpoint)}: ${error.contentTypeAnnotation} is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
    }

    private fun parseAccept(endpoint: KClass<out RestEndpoint<*, *>>): ContentType? {
      val annotations = endpoint.findAnnotations<RestEndpoint.Accept>()
      if (annotations.isEmpty()) return null
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "Endpoint ${endpoint.qualifiedName} cannot define multiple of" +
          " @${RestEndpoint::class.simpleName}.${RestEndpoint.Accept::class.simpleName}."
      }
      try {
        return ContentType.parse(annotation.value)
      } catch (e: BadContentTypeFormatException) {
        val eMessage = buildString {
          append("${error.endpoint(endpoint)}: ${error.acceptAnnotation} is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
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
