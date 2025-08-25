package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A REST endpoint template instance represents a specific subclass of [RestEndpoint].
 * In fact, it can be created from a [RestEndpoint] class reference using [RestEndpointTemplate.create].
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
      val definition = definition()
      return RestEndpointTemplate(
        method = method(definition),
        path = path(definition),
        query = query(),
        contentType = contentType(definition),
        accept = accept(definition),
      )
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [RestEndpoint.Path]
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
    private fun definition(): RestEndpoint.Definition {
      val endpoint = routing.endpoint.kotlinClass
      val annotations = endpoint.findAnnotations<RestEndpoint.Definition>()
      require(annotations.isNotEmpty()) {
        "${error.endpoint()} must define ${error.definition}."
      }
      val annotation = annotations.singleOrNull()
      requireNotNull(annotation) {
        "${error.endpoint()} cannot define multiple of ${error.definition}."
      }
      return annotation
    }

    private fun method(definition: RestEndpoint.Definition): HttpMethod =
      HttpMethod.parse(definition.method.uppercase())

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun path(definition: RestEndpoint.Definition): RestEndpointTemplatePath =
      RestEndpointTemplatePath.from(definition.path)

    context(error: RestEndpointTemplateErrorBuilder, params: RestEndpointTemplateParams, routing: KairoRouting<*>)
    private fun query(): RestEndpointTemplateQuery {
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
    private fun contentType(definition: RestEndpoint.Definition): ContentType? {
      if (definition.contentType.isEmpty()) return null
      try {
        return ContentType.parse(definition.contentType)
      } catch (e: BadContentTypeFormatException) {
        val eMessage = buildString {
          append("${error.endpoint()} content type is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(eMessage, e)
      }
    }

    context(error: RestEndpointTemplateErrorBuilder, routing: KairoRouting<*>)
    private fun accept(definition: RestEndpoint.Definition): ContentType? {
      if (definition.accept.isEmpty()) return null
      try {
        return ContentType.parse(definition.accept)
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
