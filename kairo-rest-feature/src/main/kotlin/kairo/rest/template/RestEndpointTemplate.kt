package kairo.rest.template

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import java.util.concurrent.ConcurrentHashMap
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.endpoint.hasBody
import kairo.rest.printer.PrettyRestEndpointPrinter
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
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
  val path: RestEndpointPath,
  val query: RestEndpointQuery,
  val contentType: ContentType?,
  val accept: ContentType?,
) {
  override fun toString(): String =
    "RestEndpointTemplate(${PrettyRestEndpointPrinter.write(this)})"

  public companion object {
    private val cache: MutableMap<KClass<out RestEndpoint<*, *>>, RestEndpointTemplate> = ConcurrentHashMap()

    public fun from(endpointKClass: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate =
      cache.computeIfAbsent(endpointKClass) {
        build(endpointKClass)
      }

    private fun build(endpointKClass: KClass<out RestEndpoint<*, *>>): RestEndpointTemplate {
      logger.debug { "Building REST endpoint template for endpoint $endpointKClass." }
      require(endpointKClass.isData) {
        "REST endpoint ${endpointKClass.qualifiedName!!}" +
          " must be a data class or data object."
      }
      validateParams(endpointKClass)
      val result = RestEndpointTemplate(
        method = parseMethod(endpointKClass),
        path = parsePath(endpointKClass),
        query = parseQuery(endpointKClass),
        contentType = parseContentType(endpointKClass),
        accept = parseAccept(endpointKClass),
      )
      logger.debug { "Built REST endpoint template $result for endpoint $endpointKClass." }
      return result
    }

    /**
     * This method only ensures that params have the right annotations.
     * Param specifics and how their annotations relate to class-level annotations such as [RestEndpoint.Path]
     * are validated within the appropriate parse methods in this class.
     */
    private fun validateParams(endpointKClass: KClass<out RestEndpoint<*, *>>) {
      val params = getAllParams(endpointKClass)
      params.forEach { param ->
        val isPath = param.hasAnnotation<RestEndpoint.PathParam>()
        val isQuery = param.hasAnnotation<RestEndpoint.QueryParam>()
        if (param.name == RestEndpoint<*, *>::body.name) {
          require(!isPath && !isQuery) {
            "REST endpoint ${endpointKClass.qualifiedName!!}" +
              " body cannot be param."
          }
          return@validateParams
        }
        require(!(isPath && isQuery)) {
          "REST endpoint ${endpointKClass.qualifiedName!!}" +
            " param cannot be both path and query: ${param.name!!}."
        }
        require(isPath || isQuery) {
          "REST endpoint ${endpointKClass.qualifiedName!!}" +
            " param must be path or query: ${param.name!!}."
        }
      }
    }

    private fun parseMethod(endpointKClass: KClass<out RestEndpoint<*, *>>): HttpMethod {
      val annotation = endpointKClass.findAnnotation<RestEndpoint.Method>()
      requireNotNull(annotation) {
        "REST endpoint ${endpointKClass.qualifiedName!!}" +
          " is missing @${RestEndpoint.Method::class.simpleName!!}."
      }
      val result = HttpMethod.parse(annotation.method) // This does not fail, even for arbitrary strings.
      result.validate(endpointKClass)
      return result
    }

    /**
     * Ensures that the types match the method.
     */
    private fun HttpMethod.validate(endpointKClass: KClass<out RestEndpoint<*, *>>) {
      when (this) {
        HttpMethod.Get, HttpMethod.Head, HttpMethod.Options -> {
          require(!endpointKClass.hasBody) {
            "REST endpoint ${endpointKClass.qualifiedName!!}" +
              " has method $value but specifies a body."
          }
        }

        HttpMethod.Patch -> {
          require(endpointKClass.hasBody) {
            "REST endpoint ${endpointKClass.qualifiedName!!}" +
              " has method $value but does not specify a body."
          }
        }

        HttpMethod.Delete, HttpMethod.Post, HttpMethod.Put -> {}

        else -> {
          throw IllegalArgumentException(
            "REST endpoint ${endpointKClass.qualifiedName!!}" +
              " has invalid method: $value.",
          )
        }
      }
    }

    private fun parsePath(endpointKClass: KClass<out RestEndpoint<*, *>>): RestEndpointPath {
      val annotation = endpointKClass.findAnnotation<RestEndpoint.Path>()
      requireNotNull(annotation) {
        "REST endpoint ${endpointKClass.qualifiedName!!}" +
          " is missing @${RestEndpoint.Path::class.simpleName!!}."
      }
      require(annotation.path.startsWith("/")) {
        "REST endpoint ${endpointKClass.qualifiedName!!}" +
          " path must start with a slash: ${annotation.path}."
      }
      try {
        val result = RestEndpointPath(
          components = annotation.path.drop(1).split('/').map { string ->
            val component = RestEndpointPath.Component.from(string)
            if (annotation.validate) component.validate()
            return@map component
          },
        )
        result.validate(endpointKClass)
        return result
      } catch (e: IllegalArgumentException) {
        val error = buildString {
          append("REST endpoint ${endpointKClass.qualifiedName!!} path is invalid.")
          e.message?.let { append(" $it") }
        }
        throw IllegalArgumentException(error, e)
      }
    }

    /**
     * Ensures that the path params specified in the constructor
     * are consistent with the param path components from the [RestEndpoint.Path] annotation.
     */
    private fun RestEndpointPath.validate(endpointKClass: KClass<out RestEndpoint<*, *>>) {
      val componentNames = components.filterIsInstance<RestEndpointPath.Component.Param>().map { it.value }
      val params = getParams<RestEndpoint.PathParam>(endpointKClass)
      params.forEach { param ->
        require(!param.type.isMarkedNullable) { "Path param must not be nullable: ${param.name!!}." }
        require(!param.isOptional) { "Path param must not be optional: ${param.name!!}." }
      }
      val paramNames = params.map { it.name!! }
      componentNames.forEachIndexed { i, componentName ->
        require(componentName !in componentNames.subList(0, i)) {
          "Duplicate path param in path: $componentName."
        }
        require(componentName in paramNames) {
          "Path param in path but not in constructor: $componentName."
        }
      }
      paramNames.forEach { paramName ->
        require(paramName in componentNames) {
          "Path param in constructor but not in path: $paramName."
        }
      }
    }

    private fun parseQuery(endpointKClass: KClass<out RestEndpoint<*, *>>): RestEndpointQuery {
      val params = getParams<RestEndpoint.QueryParam>(endpointKClass)
      params.forEach { param ->
        require(!param.isOptional) {
          "REST endpoint ${endpointKClass.qualifiedName!!} query is invalid." +
            " Query param must not be optional: ${param.name!!}."
        }
      }
      return RestEndpointQuery(
        params = params.map { param ->
          RestEndpointQuery.Param(value = param.name!!, required = !param.type.isMarkedNullable)
        },
      )
    }

    private fun parseContentType(endpointKClass: KClass<out RestEndpoint<*, *>>): ContentType? {
      val annotation = endpointKClass.findAnnotation<RestEndpoint.ContentType>()
      val hasBody = endpointKClass.hasBody
      if (hasBody) {
        requireNotNull(annotation) {
          "REST endpoint ${endpointKClass.qualifiedName!!}" +
            " requires @${RestEndpoint.ContentType::class.simpleName!!}" +
            " since it has a body."
        }
        try {
          val result = ContentType.parse(annotation.contentType)
          require(result != ContentType.Any) {
            "REST endpoint ${endpointKClass.qualifiedName!!} content type cannot be */*."
          }
          return result
        } catch (e: BadContentTypeFormatException) {
          val error = buildString {
            append("REST endpoint ${endpointKClass.qualifiedName!!} content type is invalid.")
            e.message?.let { append(" $it.") }
          }
          throw IllegalArgumentException(error, e)
        }
      }
      require(annotation == null) {
        "REST endpoint ${endpointKClass.qualifiedName!!}" +
          " may not have @${RestEndpoint.ContentType::class.simpleName!!}" +
          " since it does not have a body."
      }
      return null
    }

    private fun parseAccept(endpointKClass: KClass<out RestEndpoint<*, *>>): ContentType? {
      val annotation = endpointKClass.findAnnotation<RestEndpoint.Accept>()
      requireNotNull(annotation) {
        "REST endpoint ${endpointKClass.qualifiedName!!} requires @${RestEndpoint.Accept::class.simpleName!!}."
      }
      try {
        val result = ContentType.parse(annotation.accept)
        if (result == ContentType.Any) return null
        return result
      } catch (e: BadContentTypeFormatException) {
        val error = buildString {
          append("REST endpoint ${endpointKClass.qualifiedName!!} accept is invalid.")
          e.message?.let { append(" $it.") }
        }
        throw IllegalArgumentException(error, e)
      }
    }

    private inline fun <reified T : Annotation> getParams(
      endpointKClass: KClass<out RestEndpoint<*, *>>,
    ): List<KParameter> {
      val params = getAllParams(endpointKClass)
      return params.filter { parameter -> parameter.hasAnnotation<T>() }
    }

    private fun getAllParams(
      endpointKClass: KClass<out RestEndpoint<*, *>>,
    ): List<KParameter> {
      if (endpointKClass.objectInstance != null) return emptyList()
      val constructor = checkNotNull(endpointKClass.primaryConstructor) {
        "Data classes always have primary constructors."
      }
      return constructor.valueParameters
    }
  }
}
