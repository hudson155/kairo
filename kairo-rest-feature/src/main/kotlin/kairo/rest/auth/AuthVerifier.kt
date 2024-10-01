package kairo.rest.auth

import io.ktor.http.auth.HttpAuthHeader

/**
 * A verifier is an arbitrary mechanism to obtain a [Principal]
 *
 * WARNING: Be careful not to log sensitive data in this class.
 */
public abstract class AuthVerifier<out P : Principal> {
  public abstract val schemes: List<String>

  public abstract fun verify(authHeader: HttpAuthHeader.Single): P
}
