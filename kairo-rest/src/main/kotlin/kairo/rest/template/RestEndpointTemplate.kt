package kairo.rest.template

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.rest.KairoRouting
import kairo.rest.RestEndpoint
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A REST endpoint template instance represents a specific subclass of [kairo.rest.RestEndpoint].
 * In fact, it can be created from a [kairo.rest.RestEndpoint] class reference using [RestEndpointTemplate.create].
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
    context(routing: KairoRouting<*>)
    public fun create(): RestEndpointTemplate {
      logger.debug { "Building REST endpoint template (endpoint=${routing.endpoint.kotlinClass})." }
      val template = with(RestEndpointTemplateErrorBuilder) {
        with(RestEndpointTemplateParams.create()) {
          build()
        }
      }
      logger.debug { "Built REST endpoint template (endpoint=${routing.endpoint.kotlinClass}, template=$template)." }
      return template
    }

    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams, routing: KairoRouting<*>)
    private fun build(): RestEndpointTemplate {
      val endpoint = routing.endpoint.kotlinClass
      require(endpoint.isData) { "${error.endpoint()} must be a data class or data object." }
      validateParams()
      return RestEndpointTemplate(
        method = parseMethod(),
        path = parsePath(),
        query = parseQuery(),
        contentType = parseContentType(),
        accept = parseAccept(),
      )
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [kairo.rest.RestEndpoint.Path]
     * are validated within the appropriate parse methods in this class.
     */
    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams, routing: KairoRouting<*>)
    private fun validateParams() {
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "${error.endpoint()} body cannot be param."
          }
          return@validateParams
        }
        require(!(isPath && isQuery)) {
          "${error.endpoint()} param cannot be both path and query (param=${param.name})."
        }
        require(isPath || isQuery) {
          "${error.endpoint()} param must be path or query (param=${param.name})."
        }
      }
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun parseMethod(): HttpMethod {
      val endpoint = routing.endpoint.kotlinClass
      val annotations = endpoint.findAnnotations<RestEndpoint.Method>()
      require(annotations.isNotEmpty()) {
        "${error.endpoint()} must define ${error.methodAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.endpoint()} cannot define multiple of ${error.methodAnnotation}."
      }
      return HttpMethod.parse(annotation.value.uppercase())
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun parsePath(): RestEndpointTemplatePath {
      val endpoint = routing.endpoint.kotlinClass
      val annotations = endpoint.findAnnotations<RestEndpoint.Path>()
      require(annotations.isNotEmpty()) {
        "${error.endpoint()} must define ${error.pathAnnotation}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.endpoint()} cannot define multiple of ${error.pathAnnotation}."
      }
      require(annotation.value.startsWith('/')) {
        "${error.endpoint()} must start with a slash."
      }
      return RestEndpointTemplatePath.from(annotation.value)
    }

    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams, routing: KairoRouting<*>)
    private fun parseQuery(): RestEndpointTemplateQuery {
      val params = params.filter { it.hasAnnotation<RestEndpoint.QueryParam>() }
      params.forEach { param ->
        require(!param.isOptional) {
          "${error.endpoint()} query param must not be optional (param=${param.name})."
        }
      }
      return RestEndpointTemplateQuery(
        params.map { param ->
          RestEndpointTemplateQuery.Param(value = param.name!!, required = !param.type.isMarkedNullable)
        },
      )
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun parseContentType(): ContentType? {
      val endpoint = routing.endpoint.kotlinClass
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
          append("${error.endpoint()} content type is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun parseAccept(): ContentType? {
      val endpoint = routing.endpoint.kotlinClass
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
          append("${error.endpoint()} accept type is invalid.")
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
