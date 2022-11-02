package limber.auth

import java.util.UUID

public data class OrganizationClaim(
  val guid: UUID,
)

/**
 * Protects an endpoint to only members of the organization.
 */
public data class OrganizationAuth(val organizationGuid: UUID) : Auth() {
  override fun authorize(context: RestContext): Boolean {
    val organization = context.getClaim<OrganizationClaim>("organization") ?: return false
    return organization.guid == organizationGuid
  }
}
