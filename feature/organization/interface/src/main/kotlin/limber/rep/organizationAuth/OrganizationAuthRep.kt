package limber.rep.organizationAuth

import limber.validation.Auth0OrganizationIdValidator
import java.util.UUID

public data class OrganizationAuthRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val auth0OrganizationId: String,
) {
  public data class Creator(
    @Auth0OrganizationIdValidator val auth0OrganizationId: String,
  )
}
