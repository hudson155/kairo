package limber.rep.organizationAuth

import limber.validation.Auth0OrganizationNameValidator
import java.util.UUID

public data class OrganizationAuthRep(
  val guid: UUID,
  val organizationGuid: UUID,
  val auth0OrganizationId: String,
  val auth0OrganizationName: String,
) {
  public data class Creator(
    @Auth0OrganizationNameValidator val auth0OrganizationName: String,
  )
}
