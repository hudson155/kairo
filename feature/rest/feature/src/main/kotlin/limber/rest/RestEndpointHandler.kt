package limber.rest

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import limber.rest.endpointTemplate.RestEndpointTemplate
import kotlin.reflect.KClass

public abstract class RestEndpointHandler<E : RestEndpoint, R : Any?>(endpoint: KClass<E>) {
  public val template: RestEndpointTemplate<E> = RestEndpointTemplate.from(endpoint)

  /**
   * Handles a REST call that comes through Ktor.
   * This is either an external call, or one from a RestClient.
   */
  internal suspend fun handle(call: ApplicationCall) {
    val endpoint = template.endpoint(call)
    val result = handle(endpoint) ?: throw NotFoundException()
    call.respond<Any>(status(result), result)
  }

  /**
   * Handles a direct call.
   * This is one from a LocalClient.
   */
  public abstract suspend fun handle(endpoint: E): R

  public open fun status(result: R): HttpStatusCode =
    HttpStatusCode.OK
}
