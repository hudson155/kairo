package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

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
      add("[${contentType.toString()} -> ${accept.toString()}]")
      add(method.value)
      add(
        path.components.joinToString("/", prefix = "/") { component ->
          when (component) {
            is RestEndpointTemplatePath.Component.Constant -> component.value
            is RestEndpointTemplatePath.Component.Param -> ":${component.value}"
          }
        },
      )
      if (query.params.isNotEmpty()) {
        add(
          query.params.joinToString { (value, required) ->
            buildString {
              append(value)
              if (!required) append('?')
            }
          },
        )
      }
    }.joinToString(" ")
    return "RestEndpointTemplate(value='$value')"
  }

  public companion object {
    public fun from(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      logger.debug { "Building REST endpoint (endpoint=$endpoint)." }
      val template = with(RestEndpointTemplateErrorBuilder) {
        with(RestEndpointTemplateParams.from(endpoint)) {
          build(endpoint)
        }
      }
      logger.debug { "Built REST endpoint template (endpoint=$endpoint, template=$template)." }
      return template
    }

    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams)
    private fun build(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      require(endpoint.isData) {
        "${error.restEndpoint(endpoint)} must be a data class or data object."
      }
      validateParams(endpoint)
      return RestEndpointTemplate(
        method = parseMethod(endpoint),
        path = parsePath(endpoint),
        query = parseQuery(endpoint),
        contentType = parseContentType(endpoint),
        accept = parseAccept(endpoint),
      )
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [RestEndpoint.Path]
     * are validated within the appropriate parse methods in this class.
     */
    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams)
    private fun validateParams(endpoint: KClass<out RestEndpoint<*, *>>) {
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "${error.restEndpoint(endpoint)} body cannot be param."
          }
          return@validateParams
        }
        require(!(isPath && isQuery)) {
          "${error.restEndpoint(endpoint)} param cannot be both path and query (param=${param.name})."
        }
        require(isPath || isQuery) {
          "${error.restEndpoint(endpoint)} param must be path or query (param=${param.name})."
        }
      }
    }

    context(error: RestEndpointTemplateErrorBuilder)
    private fun parseMethod(endpoint: KClass<out RestEndpoint<*, *>>): HttpMethod {
      val annotations = endpoint.findAnnotations<RestEndpoint.Method>()
      require(annotations.isNotEmpty()) {
        "${error.restEndpoint(endpoint)} must define ${error.methodAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.restEndpoint(endpoint)} cannot define multiple of ${error.methodAnnotation}."
      }
      return HttpMethod.parse(annotation.value.uppercase())
    }

    context(error: RestEndpointTemplateErrorBuilder)
    private fun parsePath(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplatePath {
      val annotations = endpoint.findAnnotations<RestEndpoint.Path>()
      require(annotations.isNotEmpty()) {
        "${error.restEndpoint(endpoint)} must define ${error.pathAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.restEndpoint(endpoint)} cannot define multiple of ${error.pathAnnotation}."
      }
      require(annotation.value.startsWith('/')) {
        "${error.restEndpoint(endpoint)} must start with a slash."
      }
      return RestEndpointTemplatePath.from(annotation.value)
    }

    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams)
    private fun parseQuery(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplateQuery {
      val params = params.filter { it.hasAnnotation<RestEndpoint.QueryParam>() }
      params.forEach { param ->
        require(!param.isOptional) {
          "${error.restEndpoint(endpoint)} query param must not be optional (param=${param.name})."
        }
      }
      return RestEndpointTemplateQuery(
        params.map { param ->
          RestEndpointTemplateQuery.Param(value = param.name!!, required = !param.type.isMarkedNullable)
        },
      )
    }

    context(error: RestEndpointTemplateErrorBuilder)
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
          append("${error.restEndpoint(endpoint)} content type is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
    }

    context(error: RestEndpointTemplateErrorBuilder)
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
          append("${error.restEndpoint(endpoint)} accept type is invalid.")
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
      is RestEndpointTemplatePath.Component.Constant -> component.value
      is RestEndpointTemplatePath.Component.Param -> "{${component.value}}"
    }
  }
