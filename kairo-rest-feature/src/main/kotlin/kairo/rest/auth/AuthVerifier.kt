package kairo.rest.auth

import io.ktor.http.auth.HttpAuthHeader

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
internal abstract class AuthVerifier {
  abstract val schemes: List<String>

  abstract fun verify(authHeader: HttpAuthHeader.Single): Principal

  internal companion object {
    fun from(configs: List<AuthVerifierConfig>): Map<String, AuthVerifier> =
      configs
        .map { from(it) }
        .flatMap { verifier -> verifier.schemes.map { Pair(it.lowercase(), verifier) } }
        .toMap()

    fun from(config: AuthVerifierConfig): AuthVerifier =
      when (config) {
        is AuthVerifierConfig.Jwt -> JwtAuthVerifier.from(config)
      }
  }
}
