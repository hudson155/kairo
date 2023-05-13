package limber.mapper.organizationAuth

import com.google.inject.Inject
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.util.guid.GuidGenerator
import java.util.UUID

internal class OrganizationAuthMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(auth: OrganizationAuthModel): OrganizationAuthRep =
    OrganizationAuthRep(
      guid = auth.guid,
      organizationGuid = auth.organizationGuid,
      auth0OrganizationId = checkNotNull(auth.auth0OrganizationId) {
        "The Auth0 organization ID should only be null during the creation process."
      },
      auth0OrganizationName = auth.auth0OrganizationName,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: OrganizationAuthRep.Creator,
  ): OrganizationAuthModel.Creator =
    OrganizationAuthModel.Creator(
      guid = guidGenerator.generate(),
      organizationGuid = organizationGuid,
      auth0OrganizationName = creator.auth0OrganizationName,
    )
}
