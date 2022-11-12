package limber.auth

import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

private const val ORGANIZATION_CLAIM_NAME = "organization"

public data class OrganizationClaim(
  val guid: UUID,
)

/**
 * Protects an endpoint to only members of the organization.
 */
public class OrganizationAuth(private val organizationGuid: UUID) : Auth() {
  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val organization = getOrganizationClaim(context, principal)
      ?: return AuthResult.Unauthorized.noClaim(ORGANIZATION_CLAIM_NAME)

    if (organization.guid != organizationGuid) {
      return AuthResult.Failed
    }
    return AuthResult.Authorized
  }

  private fun getOrganizationClaim(
    context: RestContext,
    principal: JWTPrincipal,
  ): OrganizationClaim? =
    context.getClaim(principal, ORGANIZATION_CLAIM_NAME)
}
