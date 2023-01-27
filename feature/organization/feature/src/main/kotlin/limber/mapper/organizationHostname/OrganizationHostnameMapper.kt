package limber.mapper.organizationHostname

import com.google.inject.Inject
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.util.guid.GuidGenerator
import java.util.UUID

internal class OrganizationHostnameMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(model: OrganizationHostnameModel): OrganizationHostnameRep =
    OrganizationHostnameRep(
      guid = model.guid,
      organizationGuid = model.organizationGuid,
      hostname = model.hostname,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: OrganizationHostnameRep.Creator,
  ): OrganizationHostnameModel.Creator =
    OrganizationHostnameModel.Creator(
      guid = guidGenerator.generate(),
      organizationGuid = organizationGuid,
      hostname = creator.hostname,
    )
}
