package limber.auth

import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

private const val ORGANIZATION_CLAIM_NAME = "organization"

public data class OrganizationClaim(
  val guid: UUID,
  val permissions: List<OrganizationPermission>,
)

/**
 * Protects an endpoint to only members of the organization.
 */
public class OrganizationAuth(
  private val organizationGuid: UUID?,
  private val permission: OrganizationPermission,
) : Auth() {
  public constructor(
    organizationGuid: () -> UUID?,
    permission: OrganizationPermission,
  ) : this(organizationGuid(), permission)

  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val organization = getOrganizationClaim(context, principal)
      ?: return AuthResult.Unauthorized.noClaim(ORGANIZATION_CLAIM_NAME)

    if (organizationGuid == null) return AuthResult.Failed

    if (organization.guid != organizationGuid) {
      return AuthResult.Failed
    }
    if (permission !in organization.permissions) {
      return AuthResult.Forbidden("Missing required permission ${permission.value}.")
    }
    return AuthResult.Authorized
  }

  private fun getOrganizationClaim(
    context: RestContext,
    principal: JWTPrincipal,
  ): OrganizationClaim? =
    context.getClaim(principal, ORGANIZATION_CLAIM_NAME)
}
