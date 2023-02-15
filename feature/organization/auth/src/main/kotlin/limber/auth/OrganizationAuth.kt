package limber.auth

import java.util.UUID

/**
 * Protects an endpoint to only members of the organization.
 */
public class OrganizationAuth(
  private val permission: OrganizationPermission,
  private val organizationGuid: UUID?,
) : Auth() {
  public constructor(
    permission: OrganizationPermission,
    organizationGuid: () -> UUID?,
  ) : this(permission, organizationGuid())

  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val permissions = context.getPermissions(principal)
      ?: return AuthResult.Unauthorized.noClaim(PERMISSIONS_CLAIM_NAME)
    val permissionValue = permissions[permission.value]
      ?: return AuthResult.Forbidden("Missing required permission ${permission.value}.")

    if (organizationGuid == null || organizationGuid !in permissionValue) return AuthResult.Failed
    return AuthResult.Authorized
  }
}
