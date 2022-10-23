package limber.feature.rest.endpointTemplate

import io.ktor.http.HttpMethod
import io.ktor.server.application.ApplicationCall
import limber.feature.rest.RestEndpoint
import kotlin.reflect.KClass

/**
 * Whereas an instance of [RestEndpoint] represents a specific call to a specific endpoint,
 * an instance of [RestEndpointTemplate] represents a parameterized (templated) endpoint.
 *
 * Instances of this class are created for each bound endpoint during Server startup,
 * and are used to tell Ktor what method, parameterized path, and other endpoint metadata
 * to look for in request mapping.
 */
public data class RestEndpointTemplate<E : RestEndpoint>(
  val method: HttpMethod,
  val path: String,
  val requiredQueryParams: Set<String>,
  val parameters: suspend (call: ApplicationCall) -> Map<Parameter, Any?>,
  val endpoint: suspend (parameters: Map<Parameter, Any?>) -> E,
) {
  /**
   * For pretty printing a list of endpoints.
   */
  override fun toString(): String {
    var result = "${method.value} $path"
    if (requiredQueryParams.isNotEmpty()) {
      result += " (${requiredQueryParams.joinToString()})"
    }
    return result
  }

  /**
   * Generates an instance of [RestEndpointTemplate] from a [RestEndpoint] class.
   * The implementation is delegated to [RestEndpointTemplateBuilder].
   */
  internal companion object {
    fun <E : RestEndpoint> from(kClass: KClass<E>): RestEndpointTemplate<E> {
      val builder = RestEndpointTemplateBuilder.from(kClass)

      val method = builder.templateInstance.method
      var pathTemplate = builder.templateInstance.path
      val requiredQueryParams = mutableSetOf<String>()

      builder.argReplacements.forEach { (name, value) ->
        val split = pathTemplate.split(value)
        when (split.size) {
          1 -> requiredQueryParams += name
          2 -> pathTemplate = split.joinToString("{$name}")
          else -> error("${builder.templateInstance.path} contains more than 1 match of $value.")
        }
      }

      return RestEndpointTemplate(
        method = method,
        path = pathTemplate,
        requiredQueryParams = requiredQueryParams,
        parameters = builder::parameters,
        endpoint = builder::endpoint,
      )
    }
  }
}
