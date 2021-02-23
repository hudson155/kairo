package io.limberapp.auth.exception

/**
 * Verification exceptions are intended to show certain error messages to the end user if auth
 * fails.
 */
internal class VerificationException private constructor(
    val errorMessage: String,
    cause: Exception?,
) : Exception(errorMessage, cause) {
  companion object {
    fun malformedAuthHeader(cause: Exception): VerificationException =
        VerificationException("Malformed authorization header.", cause = cause)

    fun unsupportedAuthHeader(): VerificationException =
        VerificationException("Unsupported authorization header type.", cause = null)

    fun unknownScheme(): VerificationException =
        VerificationException("Unknown authorization header scheme.", cause = null)

    fun unknownIssuer(): VerificationException =
        VerificationException("Unknown JWT issuer.", cause = null)

    fun invalidJwt(cause: Exception): VerificationException =
        VerificationException("Invalid JWT.", cause = cause)
  }
}
