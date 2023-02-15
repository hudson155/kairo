package limber.auth

import limber.exception.AuthException

/**
 * Protects an endpoint behind a particular platform-level permission.
 */
public class PlatformPermissionAuth(
  internal val permission: PlatformPermission,
) : Auth() {
  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val permissions = context.getPermissions(principal)
      ?: return AuthResult.Unauthorized.noClaim(PERMISSIONS_CLAIM_NAME)
    val permissionValue = permissions[permission.value]
      ?: return AuthResult.Forbidden("Missing required permission ${permission.value}.")

    if (permissionValue !== PermissionValue.All) return AuthResult.Failed
    return AuthResult.Authorized
  }
}

public suspend fun auth(auth: PlatformPermissionAuth) {
  auth(auth) {
    throw AuthException(AuthException.Status.Forbidden, "Missing required permission ${auth.permission.value}.")
  }
}
