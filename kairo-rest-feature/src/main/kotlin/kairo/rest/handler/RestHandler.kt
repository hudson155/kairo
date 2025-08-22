package kairo.rest.handler

import com.google.inject.Inject
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kairo.mdc.withMdc
import kairo.reflect.KairoType
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.context.AuthContext
import kairo.rest.endpoint.RestEndpoint
import kairo.rest.exceptionHandler.ExceptionManager
import kairo.rest.exceptionHandler.respondWithError
import kairo.rest.reader.RestEndpointReader
import kairo.rest.response.CustomResponse
import kairo.rest.server.installStatusPages
import kairo.rest.template.RestEndpointTemplate
import kairo.rest.util.typeInfo
import kotlinx.coroutines.withContext

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

  public val endpointType: KairoType<E> =
    KairoType.from(RestHandler::class, 0, this::class)

  private val responseType: KairoType<Response> =
    KairoType.from(RestHandler::class, 1, this::class)

  public val template: RestEndpointTemplate = RestEndpointTemplate.from(endpointType.kotlinClass)
  public val reader: RestEndpointReader<E> = RestEndpointReader.from(endpointType.kotlinClass)

  public suspend fun handle(call: RoutingCall) {
    val endpoint = reader.endpoint(call)
    withMdc(mdc(call, endpoint)) {
      try {
        logger.debug { "Read endpoint: $endpoint." }
        val auth = Auth.from(call)
        auth(auth, endpoint)
        val response = withContext(AuthContext(auth)) {
          handle(endpoint)
        }
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

  private suspend fun auth(auth: Auth, endpoint: E) {
    val authProvider = AuthProvider(auth, injector)
    when (val authResult = authProvider.auth(endpoint)) {
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
    if (response is CustomResponse) {
      with(response) { respond() }
      return
    }
    val typeInfo = typeInfo(responseType.kotlinType)
    respond(statusCode, response, typeInfo)
  }
}
