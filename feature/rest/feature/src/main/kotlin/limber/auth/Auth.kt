package limber.auth

import limber.exception.AuthException

public abstract class Auth {
  /**
   * Indicates that an endpoint is publicly accessible.
   * Authentication is not required to access the endpoint.
   * Therefore, the endpoint will work even if no JWT is provided at all.
   */
  public object Public : Auth() {
    override fun authorize(context: RestContext): Boolean = true
  }

  /**
   * Protects an endpoint behind a particular permission.
   */
  public data class Permission(val permission: String) : Auth() {
    override fun authorize(context: RestContext): Boolean {
      val permissions = context.getClaim<List<String>>("permissions") ?: return false
      return permission in permissions
    }
  }

  public abstract fun authorize(context: RestContext): Boolean
}

public suspend fun auth(auth: Auth.Public) {
  auth(auth) { throw AuthException(authExceptionStatus(it)) }
}

public suspend fun auth(auth: Auth.Permission) {
  auth(auth) { throw AuthException(authExceptionStatus(it)) }
}

private fun authExceptionStatus(context: RestContext): AuthException.Status =
  if (context.hasPrincipal) AuthException.Status.Forbidden else AuthException.Status.Unauthorized
