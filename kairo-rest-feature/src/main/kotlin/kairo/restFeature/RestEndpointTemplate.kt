package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A REST endpoint template instance represents a specific subclass of [RestEndpoint].
 * In fact, it can be created from a [RestEndpoint] class reference using [RestEndpointTemplate.parse].
 */
internal data class RestEndpointTemplate(
  val method: HttpMethod,
  val path: RestEndpointPath,
  val query: RestEndpointQuery,
  val contentType: ContentType?,
  val accept: ContentType,
) {
  override fun toString(): String =
    PrettyRestEndpointWriter.write(this)

  internal companion object {
    fun parse(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      logger.debug { "Building REST endpoint template for endpoint $endpoint." }
      require(endpoint.isData) { "REST endpoint ${endpoint.qualifiedName!!} must be a data class or data object." }
      validateParams(endpoint)
      val result = RestEndpointTemplate(
        method = parseMethod(endpoint),
        path = parsePath(endpoint),
        query = parseQuery(endpoint),
        contentType = parseContentType(endpoint),
        accept = parseAccept(endpoint),
      )
      logger.debug { "Built REST endpoint template $result for endpoint $endpoint." }
      return result
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [RestEndpoint.Path]
     * are validated within the appropriate parse methods in this class.
     */
    private fun validateParams(endpoint: KClass<out RestEndpoint<*, *>>) {
      val params = getAllParams(endpoint)
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) { "REST endpoint ${endpoint.qualifiedName!!} body cannot be param." }
          return
        }
        require(!(isPath && isQuery)) {
          "REST endpoint ${endpoint.qualifiedName!!} param cannot be both path and query: ${param.name!!}."
        }
        require(isPath || isQuery) {
          "REST endpoint ${endpoint.qualifiedName!!} param must be path or query: ${param.name!!}."
        }
      }
    }

    private fun parseMethod(endpoint: KClass<out RestEndpoint<*, *>>): HttpMethod {
      val annotation = endpoint.findAnnotation<RestEndpoint.Method>()
      requireNotNull(annotation) {
        "REST endpoint ${endpoint.qualifiedName!!} is missing @${RestEndpoint.Method::class.simpleName!!}."
      }
      val result = HttpMethod.parse(annotation.method) // This does not fail, even for arbitrary strings.
      result.validate(endpoint)
      return result
    }

    /**
     * Ensures that the types match the method.
     */
    private fun HttpMethod.validate(endpoint: KClass<out RestEndpoint<*, *>>) {
      when (this) {
        HttpMethod.Get, HttpMethod.Delete, HttpMethod.Head, HttpMethod.Options -> {
          require(!endpoint.hasBody) {
            "REST endpoint ${endpoint.qualifiedName!!} has method $value but specifies a body."
          }
        }

        HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch -> {
          require(endpoint.hasBody) {
            "REST endpoint ${endpoint.qualifiedName!!} has method $value but specifies a body."
          }
        }

        else -> {
          throw IllegalArgumentException("REST endpoint ${endpoint.qualifiedName!!} has invalid method: $value.")
        }
      }
    }

    private fun parsePath(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointPath {
      val annotation = endpoint.findAnnotation<RestEndpoint.Path>()
      requireNotNull(annotation) {
        "REST endpoint ${endpoint.qualifiedName!!} is missing @${RestEndpoint.Path::class.simpleName!!}."
      }
      require(annotation.path.startsWith("/")) {
        "REST endpoint ${endpoint.qualifiedName!!} path must start with a slash: ${annotation.path}."
      }
      try {
        val result = RestEndpointPath(
          components = annotation.path.drop(1).split('/').map { RestEndpointPath.Component.from(it) },
        )
        result.validate(endpoint)
        return result
      } catch (e: IllegalArgumentException) {
        val error = buildString {
          append("REST endpoint ${endpoint.qualifiedName!!} path is invalid.")
          e.message?.let { append(" $it") }
        }
        throw IllegalArgumentException(error, e)
      }
    }

    /**
     * Ensures that the path params specified in the constructor
     * are consistent with the param path components from the [RestEndpoint.Path] annotation.
     */
    private fun RestEndpointPath.validate(endpoint: KClass<out RestEndpoint<*, *>>) {
      val componentNames = components.filterIsInstance<RestEndpointPath.Component.Param>().map { it.value }
      val params = getParams<RestEndpoint.PathParam>(endpoint)
      params.forEach { param ->
        require(!param.type.isMarkedNullable) { "Path param must not be nullable: ${param.name!!}." }
        require(!param.isOptional) { "Path param must not be optional: ${param.name!!}." }
      }
      val paramNames = params.map { it.name!! }
      componentNames.forEachIndexed { i, componentName ->
        require(componentName !in componentNames.subList(0, i)) { "Duplicate path param in path: $componentName." }
        require(componentName in paramNames) { "Path param in path but not in constructor: $componentName." }
      }
      paramNames.forEach { paramName ->
        require(paramName in componentNames) { "Path param in constructor but not in path: $paramName." }
      }
    }

    private fun parseQuery(endpoint: KClass<out RestEndpoint<*, *>>): RestEndpointQuery {
      val params = getParams<RestEndpoint.QueryParam>(endpoint)
      params.forEach { param ->
        "REST endpoint ${endpoint.qualifiedName!!} query is invalid. Query param must not be optional: ${param.name!!}."
      }
      return RestEndpointQuery(
        params = params.map { param ->
          RestEndpointQuery.Param(value = param.name!!, required = !param.type.isMarkedNullable)
        },
      )
    }

    private fun parseContentType(endpoint: KClass<out RestEndpoint<*, *>>): ContentType? {
      val annotation = endpoint.findAnnotation<RestEndpoint.ContentType>()
      val hasBody = endpoint.hasBody
      if (hasBody) {
        requireNotNull(annotation) {
          "REST endpoint ${endpoint.qualifiedName!!} requires @${RestEndpoint.ContentType::class.simpleName!!}" +
            " since it has a body."
        }
        try {
          return ContentType.parse(annotation.contentType)
        } catch (e: BadContentTypeFormatException) {
          val error = buildString {
            append("REST endpoint ${endpoint.qualifiedName!!} content type is invalid.")
            e.message?.let { append(" $it.") }
          }
          throw IllegalArgumentException(error, e)
        }
      } else {
        require(annotation == null) {
          "REST endpoint ${endpoint.qualifiedName!!} may not have @${RestEndpoint.ContentType::class.simpleName!!}" +
            " since it does not have a body."
        }
        return null
      }
    }

    private fun parseAccept(endpoint: KClass<out RestEndpoint<*, *>>): ContentType {
      val annotation = endpoint.findAnnotation<RestEndpoint.Accept>()
      requireNotNull(annotation) {
        "REST endpoint ${endpoint.qualifiedName!!} requires @${RestEndpoint.Accept::class.simpleName!!}."
      }
      try {
        return ContentType.parse(annotation.accept)
      } catch (e: BadContentTypeFormatException) {
        val error = buildString {
          append("REST endpoint ${endpoint.qualifiedName!!} accept is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(error, e)
      }
    }

    private inline fun <reified T : Annotation> getParams(
      endpoint: KClass<out RestEndpoint<*, *>>,
    ): List<KParameter> {
      val params = getAllParams(endpoint)
      return params.filter { parameter -> parameter.hasAnnotation<T>() }
    }

    private fun getAllParams(
      endpoint: KClass<out RestEndpoint<*, *>>,
    ): List<KParameter> {
      if (endpoint.objectInstance != null) return emptyList()
      val constructor = checkNotNull(endpoint.primaryConstructor) { "Data classes always have primary constructors." }
      return constructor.valueParameters
    }
  }
}
