package kairo.rest.auth

import com.auth0.jwt.interfaces.DecodedJWT

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public abstract class JwtAuthMechanism {
  public abstract val issuers: List<String?>

  public abstract fun verify(decodedJwt: DecodedJWT)
}
