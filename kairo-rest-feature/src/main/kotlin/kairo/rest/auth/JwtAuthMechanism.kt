package kairo.rest.auth

import com.auth0.jwt.interfaces.DecodedJWT

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public abstract class JwtAuthMechanism {
  public abstract val issuers: List<String?>

  public abstract fun verify(decodedJwt: DecodedJWT)

  public companion object {
    public fun from(configs: List<JwtAuthMechanismConfig>): Map<String?, JwtAuthMechanism> =
      configs
        .map { from(it) }
        .flatMap { mechanism -> mechanism.issuers.map { Pair(it, mechanism) } }
        .toMap()

    public fun from(config: JwtAuthMechanismConfig): JwtAuthMechanism =
      when (config) {
        is JwtAuthMechanismConfig.Jwt -> JwtJwtAuthMechanism.from(config)
      }
  }
}
