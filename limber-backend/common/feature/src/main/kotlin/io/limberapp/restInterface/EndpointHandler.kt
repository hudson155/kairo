package io.limberapp.restInterface

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.features.conversionService
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.auth.Auth
import io.limberapp.auth.jwt.JwtPrincipal
import io.limberapp.exception.BodyRequired
import io.limberapp.exception.ForbiddenException
import io.limberapp.exception.UnauthorizedException
import io.limberapp.rep.ValidatedRep
import io.limberapp.restInterface.EndpointHandler.Handler
import io.limberapp.restInterface.exception.BodyConversionException
import io.limberapp.restInterface.exception.MissingParameterException
import io.limberapp.restInterface.exception.ParameterConversionException
import io.limberapp.restInterface.exception.ValidationException
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Each concrete [EndpointHandler] class handles requests to a single REST endpoint on the API.
 *
 *  - [EndpointHandler.endpoint] is called first. It's the only method that's given direct access to
 *   the Ktor [ApplicationCall]. Therefore, it must read both the parameters (path and query
 *   parameters) and the body from the call, storing them in an [Endpoint] instance to make them
 *   available to the rest of the handler. To read the parameters, use [getParam]. To read the body,
 *   use [getAndValidateBody] followed by [required] if the body is required.
 *
 *  - [EndpointHandler.handle] is called next. It's responsible for authorizing and performing the
 *   request, and for determining the response. Authorization is performed by calling [Handler.auth]
 *   and using [Auth] implementations within the block. There's no valid use case for not calling
 *   [Handler.auth] at least once. If it's not called, the endpoint will return a 500 error. A
 *   response can be provided by returning it from the method.
 */
abstract class EndpointHandler<E : Endpoint, R : Any>(val template: EndpointTemplate<E>) {
  inner class Handler(private val endpoint: E, private val principal: JwtPrincipal?) {
    private var authorized: Boolean = false

    internal suspend fun handle(): Pair<HttpStatusCode, R> {
      val result = handle(endpoint)
      check(authorized) {
        "Every endpoint needs to implement authorization." +
            " ${this@EndpointHandler::class.simpleName} does not."
      }
      return Pair(HttpStatusCode.OK, result)
    }

    /**
     * Call this from within the [EndpointHandler.handle] implementation as many times as necessary
     * (but as few and as early as possible) to check whether the user is authorized. If the result
     * is false, they will be forbidden.
     */
    fun auth(block: () -> Auth) {
      if (!block().authorize(principal?.jwt)) {
        if (principal == null) throw UnauthorizedException()
        throw ForbiddenException()
      }
      authorized = true
    }
  }

  suspend fun PipelineContext<Unit, ApplicationCall>.handle() {
    val endpoint = endpoint(call)

    @Suppress("UNCHECKED_CAST")
    val principal = call.authentication.principal as JwtPrincipal?
    val (statusCode, result) = Handler(endpoint, principal).handle()
    call.respond(statusCode, result as Any)
  }

  /**
   * Called as part of the [EndpointHandler] lifecycle. See the class documentation for more
   * information.
   */
  abstract suspend fun endpoint(call: ApplicationCall): E

  /**
   * Called as part of the [EndpointHandler] lifecycle. See the class documentation for more
   * information.
   */
  abstract suspend fun Handler.handle(endpoint: E): R

  /**
   * Call this from within the [EndpointHandler.handle] implementation to retrieve the body, or null
   * if no body was provided.
   */
  protected suspend inline fun <reified T : ValidatedRep> ApplicationCall.getAndValidateBody(): T? {
    @Suppress("TooGenericExceptionCaught")
    val result = try {
      receiveOrNull<T>()
    } catch (e: Exception) {
      throw BodyConversionException(e)
    }
    return result?.also {
      val validation = it.validate()
      if (!validation.isValid) throw ValidationException(validation.invalidPropertyNames)
    }
  }

  /**
   * Call this from within the [EndpointHandler.handle] implementation to convert the body from
   * nullable to non-nullable, throwing a semantically appropriate exception if it is null.
   */
  protected fun <T : ValidatedRep> T?.required(): T {
    this ?: throw BodyRequired()
    return this
  }

  /**
   * Call this from within the [EndpointHandler.handle] implementation to get a parameter from the
   * URL as the given type, throwing an exception if it cannot be cast to that type using the
   * application's ConversionService. This method is for required params. For optional params, use
   * the overloaded method instead.
   */
  protected fun <T : Any> ApplicationCall.getParam(kClass: KClass<T>, name: String): T =
      getParam(kClass, name, optional = true) ?: throw MissingParameterException(name)

  /**
   * Call this from within the [EndpointHandler.handle] implementation to get a parameter from the
   * URL as the given type, throwing an exception if it cannot be cast to that type using the
   * application's ConversionService. This method is for optional params ([optional] must be true).
   * For required params, use the overloaded method instead.
   */
  protected fun <T : Any> ApplicationCall.getParam(
      kClass: KClass<T>,
      name: String,
      optional: Boolean = false,
  ): T? {
    check(optional)
    val values = parameters.getAll(name) ?: return null
    @Suppress("TooGenericExceptionCaught")
    return try {
      kClass.cast(application.conversionService.fromValues(values, kClass.java))
    } catch (e: Exception) {
      throw ParameterConversionException(name, e)
    }
  }
}
