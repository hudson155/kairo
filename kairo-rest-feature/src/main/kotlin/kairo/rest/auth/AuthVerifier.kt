package kairo.rest.auth

import io.ktor.http.auth.HttpAuthHeader

/**
 * A verifier is a mechanism to
 *
 * WARNING: Be careful not to log sensitive data in this class.
 */
public abstract class AuthVerifier {
  public abstract val schemes: List<String>

  public abstract fun verify(authHeader: HttpAuthHeader.Single): Principal
}
