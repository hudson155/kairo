package kairo.rest.auth

import io.ktor.http.auth.HttpAuthHeader

/**
 * A verifier is an arbitrary mechanism to obtain a [Principal]
 *
 * WARNING: Be careful not to log sensitive data in this class.
 */
public abstract class AuthVerifier<out P : Principal> {
  /**
   * The authorization header schemes supported by this verifier.
   * For example, "Bearer".
   */
  public abstract val schemes: List<String>

  public abstract fun verify(authHeader: HttpAuthHeader.Single): P
}
