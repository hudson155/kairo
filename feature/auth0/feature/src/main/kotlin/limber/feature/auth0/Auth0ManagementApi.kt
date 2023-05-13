package limber.feature.auth0

import limber.feature.auth0.rep.Auth0OrganizationRep

public interface Auth0ManagementApi {
  public fun createOrganization(creator: Auth0OrganizationRep.Creator): Auth0OrganizationRep

  public fun updateOrganization(
    organizationId: String,
    update: Auth0OrganizationRep.Update,
  ): Auth0OrganizationRep

  public fun deleteOrganization(organizationId: String)
}
