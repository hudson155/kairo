package kairo.rest.auth

import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.RoutingRequest
import kairo.exception.KairoException
import kairo.rest.exception.EndpointAlwaysDenies

/**
 * This is the receiver used for auth on REST handlers.
 */
@Suppress("ConvertObjectToDataObject", "UseDataClass")
public class Auth(
  public val request: RoutingRequest,
  public val principal: Principal?,
) {
  /**
   * Authorization must return [Result].
   * Authorization should avoid throwing, and prefer to return [Result.Exception].
   */
  public sealed class Result {
    public object Success : Result()

    public class Exception(public val e: KairoException) : Result()
  }

  public companion object {
    internal fun from(call: RoutingCall): Auth =
      Auth(
        request = call.request,
        principal = call.principal<Principal>(),
      )
  }
}

/**
 * Returns the first non-successful auth result, or success.
 */
@Suppress("UnusedReceiverParameter")
public fun AuthProvider.all(authResults: List<Auth.Result>): Auth.Result {
  authResults.forEach { auth ->
    if (auth != Auth.Result.Success) return@all auth
  }
  return Auth.Result.Success
}

@Suppress("UnusedReceiverParameter")
public fun AuthProvider.all(vararg authResults: Auth.Result): Auth.Result =
  all(authResults.toList())

@Suppress("UnusedReceiverParameter")
public fun AuthProvider.all(block: MutableList<Auth.Result>.() -> Unit): Auth.Result =
  all(buildList(block))

/**
 * Allows all requests, including unauthenticated requests without an auth principal at all.
 */
@Suppress("UnusedReceiverParameter")
public fun AuthProvider.public(): Auth.Result =
  Auth.Result.Success

/**
 * Denies all requests.
 */
@Suppress("UnusedReceiverParameter")
public fun AuthProvider.deny(): Auth.Result =
  Auth.Result.Exception(EndpointAlwaysDenies())

public inline fun overriddenBy(result: Auth.Result, block: (result: Auth.Result) -> Unit) {
  if (result == Auth.Result.Success) {
    block(result)
  }
}

/**
 * Returns successful if either [this] or any of [alternatives] are successful.
 * [this] is separated from [alternatives] because in an unsuccessful evaluation,
 * only [this]'s exception is thrown.
 */
public fun Auth.Result.overriddenBy(vararg alternatives: Auth.Result): Auth.Result {
  if (this == Auth.Result.Success) return this
  alternatives.firstOrNull { it == Auth.Result.Success }?.let { return@overriddenBy it }
  return this
}
