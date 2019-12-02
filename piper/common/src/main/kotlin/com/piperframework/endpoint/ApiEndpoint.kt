package com.piperframework.endpoint

import com.piperframework.authorization.PiperAuthorization
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.exception.exception.BadRequestException
import com.piperframework.exception.exception.ForbiddenException
import com.piperframework.exception.exception.NotFoundException
import com.piperframework.rep.ValidatedRep
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.conversionService
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Each ApiEndpoint class handles requests to a single endpoint (unique by path and method) of the API. The handler() is
 * called for each request.
 */
abstract class ApiEndpoint<P : Principal, Command : AbstractCommand, ResponseType : Any?>(
    private val application: Application,
    private val pathPrefix: String,
    private val endpointConfig: EndpointConfig
) {

    /**
     * Called for each request to the endpoint, to determine the command. The implementation should get all request
     * parameters (if appropriate) and receive the body (if appropriate). This is the only time in the ApiEndpoint
     * lifecycle that a method will be given access to the Ktor ApplicationCall.
     */
    abstract suspend fun determineCommand(call: ApplicationCall): Command

    /**
     * Called for each request to the endpoint, to specify the authorization check to be used. This should handle
     * authorization for most endpoints.
     */
    abstract fun authorization(command: Command): PiperAuthorization<P>

    /**
     * Called for each request to the endpoint, to specify an additional authorization check to be used after the
     * request has been handled. This can handle authorization for endpoints where alternative identifiers are used and
     * it's not possible to know in advance which endpoint. This should only be used for GET endpoints, or else the
     * operation will have already been performed.
     */
    open fun secondaryAuthorization(response: ResponseType): PiperAuthorization<P>? = null

    /**
     * Called for each request to the endpoint, to handle the execution. This method is the meat and potatoes of the
     * ApiEndpoint instance. By this point, the command has been determined and the user has been authorized. All that's
     * left is for the "actual work" to be done. However, even though this is the meat and potatoes, in a good
     * architecture this method probably has very simple implementation and delegates most of the work to the service
     * layer.
     */
    abstract suspend fun handler(command: Command): ResponseType

    /**
     * Registers the endpoint with the application to bind requests to that endpoint to this
     * ApiEndpoint instance.
     */
    fun register() {
        application.routing {
            authenticate(optional = true) {
                route(pathPrefix) { routeEndpoint() }
            }
        }
    }

    private fun Route.routeEndpoint() {
        route(endpointConfig.pathTemplate, endpointConfig.httpMethod) {
            handle {
                val command = determineCommand(call)
                val principal = call.authentication.principal as? P
                if (!authorization(command).authorize(principal)) throw ForbiddenException()
                val result = handler(command)
                val secondaryAuthorization = secondaryAuthorization(result)
                if (endpointConfig.httpMethod != HttpMethod.Get) {
                    // Only GET endpoints can use secondary authorization.
                    check(secondaryAuthorization == null)
                }
                if (secondaryAuthorization(result)?.authorize(principal) == false) throw ForbiddenException()
                if (result == null) throw NotFoundException()
                else call.respond(result)
            }
        }
    }

    protected suspend inline fun <reified T : ValidatedRep> ApplicationCall.getAndValidateBody() =
        receive<T>().apply { validate() }

    /**
     * Gets a parameter from the URL as the given type, throwing an exception if it cannot be cast to that type using
     * the application's ConversionService.
     */
    protected fun <T : Any> Parameters.getAsType(clazz: KClass<T>, name: String): T {
        val values = getAll(name) ?: throw BadRequestException()
        @Suppress("TooGenericExceptionCaught")
        return try {
            clazz.cast(application.conversionService.fromValues(values, clazz.java))
        } catch (_: Exception) {
            throw BadRequestException()
        }
    }
}
