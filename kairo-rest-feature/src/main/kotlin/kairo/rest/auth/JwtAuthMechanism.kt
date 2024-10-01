package kairo.rest.auth

import com.auth0.jwt.interfaces.DecodedJWT

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
internal abstract class JwtAuthMechanism {
  abstract val issuers: List<String?>

  abstract fun verify(decodedJwt: DecodedJWT)

  internal companion object {
    fun from(configs: List<JwtAuthMechanismConfig>): Map<String?, JwtAuthMechanism> =
      configs
        .map { from(it) }
        .flatMap { mechanism -> mechanism.issuers.map { Pair(it, mechanism) } }
        .toMap()

    fun from(config: JwtAuthMechanismConfig): JwtAuthMechanism =
      when (config) {
        is JwtAuthMechanismConfig.Jwt -> JwtJwtAuthMechanism.from(config)
      }
  }
}
