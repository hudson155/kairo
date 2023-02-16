package limber.feature.auth0

public interface Auth0ManagementApi {
  public fun createOrganization(name: String): String

  public fun updateOrganization(organizationId: String, name: String?)
}
