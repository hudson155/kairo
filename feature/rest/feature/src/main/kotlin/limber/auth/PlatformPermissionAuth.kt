package limber.auth

import io.ktor.server.auth.jwt.JWTPrincipal
import limber.exception.AuthException

private const val PERMISSIONS_CLAIM_NAME = "permissions"

/**
 * Protects an endpoint behind a particular platform-level permission.
 */
public class PlatformPermissionAuth(
  internal val permission: PlatformPermission,
) : Auth() {
  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val permissions = getPermissionsClaim(context, principal)
      ?: return AuthResult.Unauthorized.noClaim(PERMISSIONS_CLAIM_NAME)

    if (permission !in permissions) {
      return AuthResult.Forbidden("Missing required permission ${permission.value}.")
    }
    return AuthResult.Authorized
  }

  private fun getPermissionsClaim(
    context: RestContext,
    principal: JWTPrincipal,
  ): List<PlatformPermission>? {
    val permissions = context.getClaim<List<PlatformPermission?>>(principal, PERMISSIONS_CLAIM_NAME) ?: return null
    return permissions.filterNotNull() // Unknown permissions are converted to null.
  }
}

public suspend fun auth(auth: PlatformPermissionAuth) {
  auth(auth) {
    throw AuthException(AuthException.Status.Forbidden, "Missing required permission ${auth.permission.value}")
  }
}
