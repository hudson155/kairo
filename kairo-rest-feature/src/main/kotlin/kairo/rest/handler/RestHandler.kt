package kairo.rest.handler

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.util.reflect.typeInfo
import kairo.reflect.typeParam
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.reader.RestEndpointReader
import kairo.rest.template.RestEndpointTemplate
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A [RestHandler] implementation is the entrypoint for a specific [RestEndpoint].
 * See this Feature's README or tests for some examples.
 */
public abstract class RestHandler<E : RestEndpoint<*, Response>, Response : Any?> {
  internal val endpoint: KClass<E> = typeParam(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.from(endpoint)
  internal val reader: RestEndpointReader<E> = RestEndpointReader.from(endpoint)

  internal suspend fun handle(call: RoutingCall) {
    val endpoint = reader.endpoint(call)
    logger.debug { "Read endpoint: $endpoint." }
    val response = handle(endpoint)
    logger.debug { "Result: ${response?.toString() ?: "null"}." }
    val statusCode = statusCode(response)
    logger.debug { "Status code: $statusCode." }
    response ?: throw NotFoundException()
    call.respond(statusCode, response)
  }

  @Suppress("SuspendFunWithCoroutineScopeReceiver")
  private suspend fun RoutingCall.respond(statusCode: HttpStatusCode, response: Response) {
    if (response is Unit) {
      respond(statusCode)
      return
    }
    respond(statusCode, response, typeInfo<Any>())
  }

  public abstract suspend fun handle(endpoint: E): Response

  protected open fun statusCode(response: Response): HttpStatusCode =
    HttpStatusCode.OK
}
