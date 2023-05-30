package limber.auth

/**
 * Protects an endpoint to only members of the organization.
 */
public class OrganizationAuth(
  private val permission: OrganizationPermission,
  private val organizationId: String?,
) : Auth() {
  public constructor(
    permission: OrganizationPermission,
    organizationId: () -> String?,
  ) : this(permission, organizationId())

  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val permissions = context.getPermissions(principal)
      ?: return AuthResult.Unauthorized.noClaim(PERMISSIONS_CLAIM_NAME)
    val permissionValue = permissions[permission.value]
      ?: return AuthResult.Forbidden("Missing required permission ${permission.value}.")

    if (organizationId == null || organizationId !in permissionValue) return AuthResult.Failed
    return AuthResult.Authorized
  }
}
