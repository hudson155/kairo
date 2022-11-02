package limber.feature.rest

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import kotlinx.coroutines.withContext
import limber.auth.RestContextFactory
import limber.auth.getRestContext
import limber.feature.rest.endpointTemplate.Parameter
import limber.feature.rest.endpointTemplate.RestEndpointTemplate
import limber.feature.rest.ktorPlugins.startTime
import limber.feature.rest.wrapper.withErrorHandling
import limber.feature.rest.wrapper.withMdc
import mu.KLogger
import mu.KotlinLogging
import kotlin.reflect.KClass

public abstract class RestEndpointHandler<E : RestEndpoint, R : Any?>(endpoint: KClass<E>) {
  private val logger: KLogger = KotlinLogging.logger {}

  @Inject
  private lateinit var restContextFactory: RestContextFactory

  @Inject
  private lateinit var validator: Validator

  public val template: RestEndpointTemplate<E> = RestEndpointTemplate.from(endpoint)

  /**
   * Handles a REST call that comes through Ktor.
   * This is either an external call, or one from a RestClient.
   */
  internal suspend fun handle(call: ApplicationCall) {
    val parameters = template.parameters(call)
    val endpoint = template.endpoint(parameters)
    withMdc(mdc(call, parameters, endpoint)) {
      withErrorHandling(call) {
        val result = withContext(restContextFactory.create(call)) {
          val result = handle(endpoint)
          if (!getRestContext().hasAttemptdAuthorization) {
            // The purpose of this check is to help discover DURING TESTING
            // that an endpoint doesn't implement authorization.
            // It should never happen in production.
            error("This endpoint does not implement authorization.")
          }
          return@withContext result
        }
        result ?: throw NotFoundException()
        call.respond<Any>(status(result), result)
      }
      logger.info {
        "${call.response.status() ?: "Unhandled"}: ${call.request.httpMethod.value} - ${call.request.path()}"
      }
    }
  }

  /**
   * Handles a direct call.
   * This is one from a LocalClient.
   */
  public suspend fun handle(endpoint: E): R {
    validator.validate(endpoint).let { if (it.isNotEmpty()) throw ConstraintViolationException(it) }
    return handler(endpoint)
  }

  private fun mdc(call: ApplicationCall, parameters: Map<Parameter, Any?>, endpoint: E): Map<String, Any> {
    // These are included by default in every REST call.
    val restMdc = mapOf(
      "callStartTime" to call.startTime,
      "httpMethod" to endpoint.method,
      "httpPathTemplate" to template.path,
    )

    // Path parameters are extracted and included automatically.
    val endpointMdc = parameters
      .filterKeys { it is Parameter.Path }
      .mapNotNull { (key, value) -> value?.let { Pair(key.name, it) } }

    // Endpoints can add more custom MDC if they want.
    val customMdc = mdc(endpoint)

    return restMdc + endpointMdc + customMdc
  }

  protected open fun mdc(endpoint: E): Map<String, Any> = emptyMap()

  protected abstract suspend fun handler(endpoint: E): R

  public open fun status(result: R): HttpStatusCode = HttpStatusCode.OK
}
