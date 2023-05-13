package limber.feature.auth0.rep

import com.auth0.json.mgmt.organizations.Organization

public data class Auth0OrganizationRep(
  val id: String,
  val name: String,
) {
  internal constructor(organization: Organization) : this(
    id = organization.id,
    name = organization.name,
  )

  public data class Creator(
    val name: String,
  )

  public data class Update(
    val name: String? = null,
  )
}
