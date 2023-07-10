package limber.rep.organizationAuth

import com.fasterxml.jackson.annotation.JsonInclude
import limber.validation.Auth0OrganizationNameValidator

public data class OrganizationAuthRep(
  val id: String,
  val organizationId: String,
  val auth0OrganizationId: String,
  val auth0OrganizationName: String,
) {
  public data class Creator(
    @Auth0OrganizationNameValidator val auth0OrganizationName: String,
  )

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  public data class Update(
    @Auth0OrganizationNameValidator val auth0OrganizationName: String? = null,
  )
}
