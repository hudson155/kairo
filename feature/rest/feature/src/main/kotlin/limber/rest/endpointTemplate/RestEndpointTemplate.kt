package limber.rest.endpointTemplate

import io.ktor.http.HttpMethod
import io.ktor.server.application.ApplicationCall
import limber.rest.RestEndpoint
import kotlin.reflect.KClass

/**
 * Whereas an instance of [RestEndpoint] represents a specific call to a specific endpoint,
 * an instance of [RestEndpointTemplate] represents a parameterized (templated) endpoint.
 *
 * Instances of this class are created for each bound endpoint during server startup,
 * and are used to tell Ktor what method, parameterized path, and other endpoint metadata
 * to look for in request mapping.
 */
public data class RestEndpointTemplate<E : RestEndpoint>(
  val method: HttpMethod,
  val path: String,
  val parameters: suspend (call: ApplicationCall) -> Map<Parameter, Any?>,
  val endpoint: suspend (parameters: Map<Parameter, Any?>) -> E,
) {
  /**
   * For pretty printing a list of endpoints.
   */
  override fun toString(): String =
    "${method.value} $path"

  /**
   * Generates an instance of [RestEndpointTemplate] from a [RestEndpoint] class.
   * The implementation is delegated to [RestEndpointTemplateBuilder].
   */
  internal companion object {
    fun <E : RestEndpoint> from(kClass: KClass<E>): RestEndpointTemplate<E> {
      val builder = RestEndpointTemplateBuilder.from(kClass)
      return RestEndpointTemplate(
        method = builder.method,
        path = builder.pathTemplate,
        parameters = builder::parameters,
        endpoint = builder::endpoint,
      )
    }
  }
}
