package limber.mapper.organization

import com.google.inject.Inject
import limber.model.organization.OrganizationModel
import limber.rep.organization.OrganizationRep
import limber.util.guid.GuidGenerator

internal class OrganizationMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(organization: OrganizationModel): OrganizationRep =
    OrganizationRep(
      guid = organization.guid,
      name = organization.name,
    )

  operator fun invoke(creator: OrganizationRep.Creator): OrganizationModel.Creator =
    OrganizationModel.Creator(
      guid = guidGenerator.generate(),
      name = creator.name,
    )
}
