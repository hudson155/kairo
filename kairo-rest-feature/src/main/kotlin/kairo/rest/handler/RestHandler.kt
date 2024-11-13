package kairo.rest.handler

import com.google.inject.Inject
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.util.reflect.typeInfo
import kairo.mdc.withMdc
import kairo.reflect.typeParamKclass
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.exceptionHandler.ExceptionManager
import kairo.rest.exceptionHandler.respondWithError
import kairo.rest.reader.RestEndpointReader
import kairo.rest.server.installStatusPages
import kairo.rest.template.RestEndpointTemplate
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A [RestHandler] implementation is the entrypoint for a specific [RestEndpoint].
 * See this Feature's README or tests for some examples.
 */
public abstract class RestHandler<E : RestEndpoint<*, Response>, Response : Any> {
  @Inject
  private lateinit var exceptionManager: ExceptionManager

  @Inject
  private lateinit var injector: Injector

  internal val endpointKClass: KClass<E> = typeParamKclass(RestHandler::class, 0, this::class)

  internal val template: RestEndpointTemplate = RestEndpointTemplate.from(endpointKClass)
  internal val reader: RestEndpointReader<E> = RestEndpointReader.from(endpointKClass)

  internal suspend fun handle(call: RoutingCall) {
    val endpoint = reader.endpoint(call)
    withMdc(mdc(call, endpoint)) {
      try {
        logger.debug { "Read endpoint: $endpoint." }
        auth(call, endpoint)
        val response = handle(endpoint)
        logger.debug { "Result: $response." }
        val statusCode = statusCode(response)
        logger.debug { "Status code: $statusCode." }
        call.respond(statusCode, response)
      } catch (e: Exception) {
        /**
         * Doing error handling here duplicates the work done within [installStatusPages].
         * However, it is duplicated intentionally
         * so that error handling can be done within the MDC context when possible.
         */
        call.respondWithError(exceptionManager.handle(e))
      } finally {
        call.log()
      }
    }
  }

  private fun mdc(call: RoutingCall, endpoint: E): Map<String, Any?> =
    MdcGenerator(call, template, endpoint).generate() + mdc(endpoint)

  /**
   * Provides MDC to accompany default MDC.
   * Elements in the map are included in every log line.
   */
  protected open fun mdc(endpoint: E): Map<String, Any?> =
    emptyMap()

  private suspend fun auth(call: RoutingCall, endpoint: E) {
    val auth = Auth.from(call)
    val authProvider = AuthProvider(auth, injector)
    val authResult = authProvider.auth(endpoint)
    when (authResult) {
      is Auth.Result.Success -> Unit
      is Auth.Result.Exception -> throw authResult.e
    }
  }

  /**
   * Must check if the call to this endpoint is authorized.
   */
  public abstract suspend fun AuthProvider.auth(endpoint: E): Auth.Result

  /**
   * Does the endpoint's "actual work".
   * Invoked only if the call is authorized.
   */
  public abstract suspend fun handle(endpoint: E): Response

  protected open fun statusCode(response: Response): HttpStatusCode =
    HttpStatusCode.OK

  @Suppress("SuspendFunWithCoroutineScopeReceiver")
  private suspend fun RoutingCall.respond(statusCode: HttpStatusCode, response: Response) {
    if (response is Unit) {
      respond(statusCode)
      return
    }
    respond(statusCode, response, typeInfo<Any>())
  }
}
