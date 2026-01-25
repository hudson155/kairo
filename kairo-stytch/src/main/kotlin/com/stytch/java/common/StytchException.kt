@file:Suppress("InvalidPackageDeclaration")

package com.stytch.java.common

/**
 * The original [StytchException] does not provide a useful error message in the stack trace.
 */
public sealed class StytchException(
  public open val reason: Any?,
) : Exception(reason.toString()) {
  public data class Critical(
    override val reason: Throwable,
    val response: String? = null,
  ) : StytchException(reason)

  public data class Response(
    override val reason: ErrorResponse,
  ) : StytchException(reason)
}
