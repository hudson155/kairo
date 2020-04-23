package com.piperframework.endpoint

import com.piperframework.authorization.PiperAuthorization
import com.piperframework.endpoint.exception.ParameterConversionException
import com.piperframework.endpoint.exception.ValidationException
import com.piperframework.exception.exception.badRequest.BodyRequired
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.rep.ValidatedRep
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.restInterface.PiperEndpointTemplate
import com.piperframework.restInterface.forKtor
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
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Each ApiEndpoint class handles requests to a single endpoint (unique by path and method) of the API. The handler() is
 * called for each request.
 */
abstract class ApiEndpoint<P : Principal, Endpoint : PiperEndpoint, ResponseType : Any>(
    private val application: Application,
    private val pathPrefix: String,
    private val endpointTemplate: PiperEndpointTemplate
) {

    private val logger = LoggerFactory.getLogger(ApiEndpoint::class.java)

    inner class Handler(private val endpoint: Endpoint, private val principal: P?) {

        private var authorized = false

        internal suspend fun handle(): Pair<HttpStatusCode?, ResponseType> {
            val result = handle(endpoint)
            check(authorized) {
                "Every endpoint needs to implement authorization. ${this@ApiEndpoint::class.simpleName} does not."
            }
            return Pair(responseCode, result)
        }

        fun PiperAuthorization<P>.authorize() {
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
     * However, even though this is the meat and potatoes, in a good architecture this method probably has very simple
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
        logger.info("Registering ${endpointTemplate.httpMethod} ${endpointTemplate.pathTemplate}")
        application.routing {
            authenticate(optional = true) {
                route(pathPrefix) { routeEndpoint() }
            }
        }
    }

    private fun Route.routeEndpoint() {
        route(endpointTemplate.pathTemplate, endpointTemplate.httpMethod.forKtor()) {
            handle {
                val command = determineCommand(call)

                @Suppress("UNCHECKED_CAST")
                val principal = call.authentication.principal as? P
                val result = Handler(command, principal).handle()
                call.respond(result.first ?: HttpStatusCode.OK, result.second)
            }
        }
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
