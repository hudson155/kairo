package io.limberapp.framework.endpoint

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.features.MissingRequestParameterException
import io.ktor.features.NotFoundException
import io.ktor.features.ParameterConversionException
import io.ktor.features.conversionService
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.authorization.jwt.jwtFromPayload
import io.limberapp.framework.endpoint.command.AbstractCommand
import io.limberapp.framework.exception.ForbiddenException
import io.limberapp.framework.rep.ValidatedRep
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.jvm.jvmName

/**
 * Each ApiEndpoint class handles requests to a single endpoint (unique by path and method) of the
 * API. The handler() is called for each request.
 */
abstract class ApiEndpoint<Command : AbstractCommand, ResponseType : Any?>(
    private val application: Application,
    private val pathPrefix: String,
    private val endpointConfig: EndpointConfig
) {

    /**
     * Called for each request to the endpoint, to determine the command. The implementation should
     * get all request parameters (if appropriate) and receive the body (if appropriate). This is
     * the only time in the ApiEndpoint lifecycle that a method will be given access to the Ktor
     * ApplicationCall.
     */
    abstract suspend fun determineCommand(call: ApplicationCall): Command

    /**
     * Called for each request to the endpoint, to specify the authorization check to be used.
     */
    abstract fun authorization(command: Command): Authorization

    /**
     * Called for each request to the endpoint, to handle the execution. This method is the meat and
     * potatoes of the ApiEndpoint instance. By this point, the command has been determined and the
     * user has been authorized. All that's left is for the "actual work" to be done. However, even
     * though this is the meat and potatoes, in a good architecture this method probably has very
     * simple implementation and delegates most of the work to the service layer.
     */
    abstract suspend fun handler(command: Command): ResponseType

    init {
        register()
    }

    /**
     * Registers the endpoint with the application to bind requests to that endpoint to this
     * ApiEndpoint instance.
     */
    private fun register() {
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
                val jwtPayload = call.authentication.principal<JWTPrincipal>()?.payload
                val jwt = jwtFromPayload(jwtPayload)
                if (!authorization(command).authorize(jwt)) throw ForbiddenException()
                val result = handler(command)
                if (result == null) throw NotFoundException()
                else call.respond(result)
            }
        }
    }

    protected suspend inline fun <reified T : ValidatedRep> ApplicationCall.getAndValidateBody() =
        receive<T>().apply { validate() }

    /**
     * Gets a parameter from the URL as the given type, throwing an exception if it cannot be cast
     * to that type using the application's ConversionService.
     */
    protected fun <T : Any> Parameters.getAsType(clazz: KClass<T>, name: String): T {
        val values = getAll(name) ?: throw MissingRequestParameterException(name)
        @Suppress("TooGenericExceptionCaught")
        return try {
            clazz.cast(application.conversionService.fromValues(values, clazz.java))
        } catch (e: Exception) {
            throw ParameterConversionException(name, clazz.jvmName, e)
        }
    }
}
