package limber.rest

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import limber.rest.endpointTemplate.RestEndpointTemplate
import kotlin.reflect.KClass

/**
 * This class uses [Inject] combined with lateinit to access injection.
 * Constructor-based injection is typically preferred due to immutability,
 * but in this case we get the nice advantage of
 * not requiring each implementation to bloat its constructor.
 */
@Suppress("LateinitUsage")
public abstract class RestEndpointHandler<E : RestEndpoint, R : Any?>(endpoint: KClass<E>) {
  @Inject
  public lateinit var validator: Validator

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
  public suspend fun handle(endpoint: E): R {
    validator.validate(endpoint).let { if (it.isNotEmpty()) throw ConstraintViolationException(it) }
    return handler(endpoint)
  }

  protected abstract suspend fun handler(endpoint: E): R

  public open fun status(result: R): HttpStatusCode =
    HttpStatusCode.OK
}
