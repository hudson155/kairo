package limber.mapper.organizationAuth

import com.google.inject.Inject
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.util.guid.GuidGenerator
import java.util.UUID

internal class OrganizationAuthMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(model: OrganizationAuthModel): OrganizationAuthRep =
    OrganizationAuthRep(
      guid = model.guid,
      organizationGuid = model.organizationGuid,
      auth0OrganizationId = model.auth0OrganizationId,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: OrganizationAuthRep.Creator,
  ): OrganizationAuthModel.Creator =
    OrganizationAuthModel.Creator(
      guid = guidGenerator.generate(),
      organizationGuid = organizationGuid,
      auth0OrganizationId = creator.auth0OrganizationId,
    )
}
