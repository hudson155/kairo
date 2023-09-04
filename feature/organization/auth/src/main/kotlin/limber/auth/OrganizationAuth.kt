package limber.auth

/**
 * Protects an endpoint to only members of the organization.
 */
public class OrganizationAuth(
  internal val permission: OrganizationPermission,
  private val organizationId: String,
) : Auth() {
  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val permissions = context.getPermissions(principal)
      ?: return AuthResult.Unauthorized.noClaim(PERMISSIONS_CLAIM_NAME)
    val permissionValue = permissions[permission.value]
    if (permissionValue == null || organizationId !in permissionValue) {
      return AuthResult.Forbidden.missingRequiredPermission(permission.value)
    }

    return AuthResult.Authorized
  }
}
