package limber.feature.auth0

import com.google.inject.Inject
import limber.feature.auth0.rep.Auth0OrganizationRep
import limber.util.id.GuidGenerator
import limber.util.updater.update

private const val EXISTING_VALUE = "The existing value is unknown."

internal class FakeAuth0ManagementApi @Inject constructor(
  private val guidGenerator: GuidGenerator,
) : Auth0ManagementApi {
  override fun createOrganization(creator: Auth0OrganizationRep.Creator): Auth0OrganizationRep =
    Auth0OrganizationRep(
      id = guidGenerator.generate().toString(),
      name = creator.name,
      displayName = creator.displayName,
    )

  override fun updateOrganization(
    organizationId: String,
    update: Auth0OrganizationRep.Update,
  ): Auth0OrganizationRep =
    Auth0OrganizationRep(
      id = guidGenerator.generate().toString(),
      name = update(EXISTING_VALUE, update.name),
      displayName = update(EXISTING_VALUE, update.displayName),
    )

  override fun deleteOrganization(organizationId: String): Unit = Unit
}
