package limber.auth

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
    if (permissionValue != PermissionValue.All) {
      return AuthResult.Forbidden.missingRequiredPermission(permission.value)
    }

    return AuthResult.Authorized
  }
}
