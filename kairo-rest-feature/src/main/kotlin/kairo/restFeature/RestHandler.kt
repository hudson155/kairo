package kairo.restFeature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kairo.reflect.typeParam
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A [RestHandler] implementation is the entrypoint for a specific [RestEndpoint].
 * See this Feature's README or tests for some examples.
 */
public abstract class RestHandler<Endpoint : RestEndpoint<*, Response>, Response> {
  private val endpoint: KClass<Endpoint> = typeParam(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.from(endpoint)
  private val reader: RestEndpointReader<Endpoint> = RestEndpointReader.from(endpoint)

  internal suspend fun handle(call: RoutingCall) {
    val endpoint = reader.endpoint(call)
    logger.debug { "Read endpoint: $endpoint." }
    val result = handle(endpoint)
    logger.debug { "Result: $endpoint." }
    val statusCode = statusCode(result)
    logger.debug { "Status code: $endpoint." }
    result ?: throw NotFoundException()
    call.response.status(statusCode)
    call.respond<Any>(result)
  }

  protected abstract fun handle(endpoint: Endpoint): Response

  protected open fun statusCode(response: Response): HttpStatusCode =
    HttpStatusCode.OK
}
