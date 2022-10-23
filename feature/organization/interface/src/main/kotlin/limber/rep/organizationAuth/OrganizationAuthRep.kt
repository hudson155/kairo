package limber.rep.organizationAuth

import java.util.UUID

public data class OrganizationAuthRep(
  val organizationGuid: UUID,
  val guid: UUID,
  val auth0OrganizationId: String,
) {
  public data class Creator(
    val auth0OrganizationId: String,
  )
}
