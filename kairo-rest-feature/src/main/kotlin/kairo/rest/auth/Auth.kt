package kairo.rest.auth

import kairo.exception.KairoException

/**
 * This is the receiver used for auth on REST handlers.
 */
@Suppress("ConvertObjectToDataObject", "UseDataClass")
public class Auth(
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
