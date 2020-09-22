package io.limberapp.common.endpoint

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.conversionService
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.HttpAcceptRouteSelector
import io.ktor.routing.HttpMethodRouteSelector
import io.ktor.routing.ParameterRouteSelector
import io.ktor.routing.Route
import io.ktor.routing.createRouteFromPath
import io.ktor.routing.routing
import io.ktor.util.pipeline.ContextDsl
import io.limberapp.common.authorization.LimberAuthorization
import io.limberapp.common.endpoint.exception.ParameterConversionException
import io.limberapp.common.endpoint.exception.ValidationException
import io.limberapp.common.exception.exception.badRequest.BodyRequired
import io.limberapp.common.exception.exception.forbidden.ForbiddenException
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.restInterface.LimberEndpointTemplate
import io.limberapp.rep.ValidatedRep
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Each ApiEndpoint class handles requests to a single endpoint (unique by path and method) of the API. The handler() is
 * called for each request.
 */
abstract class ApiEndpoint<P : Principal, Endpoint : LimberEndpoint, ResponseType : Any>(
  private val application: Application,
  private val endpointTemplate: LimberEndpointTemplate,
) {
  private val logger = LoggerFactory.getLogger(ApiEndpoint::class.java)

  inner class Handler(private val endpoint: Endpoint, val principal: P?) {
    private var authorized = false

    internal suspend fun handle(): Pair<HttpStatusCode?, ResponseType> {
      val result = handle(endpoint)
      check(authorized) {
        "Every endpoint needs to implement authorization. ${this@ApiEndpoint::class.simpleName} does not."
      }
      return Pair(responseCode, result)
    }

    fun LimberAuthorization<P>.authorize() {
      if (!authorize(principal)) throw ForbiddenException()
      authorized = true
    }

    var responseCode: HttpStatusCode? = null
  }

  /**
   * Called for each request to the endpoint, to determine the command. The implementation should get all request
   * parameters (if appropriate) and receive the body (if appropriate). This is the only time in the ApiEndpoint
   * lifecycle that a method will be given access to the Ktor ApplicationCall.
   */
  abstract suspend fun determineCommand(call: ApplicationCall): Endpoint

  /**
   * Called for each request to the endpoint, to handle the authorization and execution. This method is the meat and
   * potatoes of the ApiEndpoint instance. By this point, the command has been determined and the user has been
   * authenticated. All that's left is for authorization to be performend and for the "actual work" to be done.
   * However, even though this is the meat and potatoes, in a good architecture this method probably has a very simple
   * implementation and delegates most of the work to the service layer.
   *
   * Note: If authorization is not performed in the implementation of this method, a 500 error will be thrown every
   * single time the endpoint is called. Nice.
   */
  abstract suspend fun Handler.handle(command: Endpoint): ResponseType

  /**
   * Registers the endpoint with the application to bind requests to that endpoint to this
   * ApiEndpoint instance.
   */
  fun register() {
    logger.info(
      listOfNotNull(
        "Registering",
        endpointTemplate.httpMethod,
        endpointTemplate.pathTemplate,
        with(endpointTemplate.requiredQueryParams) { if (isNotEmpty()) "(${joinToString()})" else null }
      ).joinToString(" ")
    )
    application.routing { authenticate(optional = true) { routeEndpoint() } }
  }

  private fun Route.routeEndpoint() {
    route(endpointTemplate) {
      handle {
        val command = determineCommand(call)

        @Suppress("UNCHECKED_CAST")
        val principal = call.authentication.principal as? P
        val result = Handler(command, principal).handle()
        call.respond(result.first ?: HttpStatusCode.OK, result.second)
      }
    }
  }

  /**
   * Builds a route to match specified [endpointTemplate].
   */
  @ContextDsl
  private fun Route.route(endpointTemplate: LimberEndpointTemplate, build: Route.() -> Unit): Route {
    var route = createRouteFromPath(endpointTemplate.pathTemplate)
    route = route.createChild(HttpAcceptRouteSelector(endpointTemplate.contentType))
    route = route.createChild(HttpMethodRouteSelector(endpointTemplate.httpMethod))
    endpointTemplate.requiredQueryParams.forEach { route = route.createChild(ParameterRouteSelector(it)) }
    return route.apply(build)
  }

  protected suspend inline fun <reified T : ValidatedRep> ApplicationCall.getAndValidateBody(): T? {
    @Suppress("TooGenericExceptionCaught")
    val result = try {
      receiveOrNull<T>()
    } catch (e: Exception) {
      throw ParameterConversionException(cause = e)
    } ?: return null
    return result.apply {
      val validation = validate()
      if (!validation.isValid) throw ValidationException(validation.firstInvalidPropertyName)
    }
  }

  protected fun <T : ValidatedRep> T?.required(): T {
    this ?: throw BodyRequired()
    return this
  }

  protected fun <T : Any> Parameters.getAsType(kClass: KClass<T>, name: String): T {
    return getAsType(kClass, name, optional = true)
      ?: throw ParameterConversionException("Missing required parameter: $name.")
  }

  /**
   * Gets a parameter from the URL as the given type, throwing an exception if it cannot be cast to that type using
   * the application's ConversionService.
   */
  protected fun <T : Any> Parameters.getAsType(kClass: KClass<T>, name: String, optional: Boolean = false): T? {
    check(optional)
    val values = getAll(name) ?: return null
    @Suppress("TooGenericExceptionCaught")
    return try {
      kClass.cast(application.conversionService.fromValues(values, kClass.java))
    } catch (e: Exception) {
      throw ParameterConversionException(cause = e)
    }
  }
}
