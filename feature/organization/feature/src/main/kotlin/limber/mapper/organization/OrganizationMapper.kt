package limber.mapper.organization

import com.google.inject.Inject
import limber.model.organization.OrganizationModel
import limber.rep.organization.OrganizationRep
import limber.util.guid.GuidGenerator

internal class OrganizationMapper @Inject constructor(
  private val guidGenerator: GuidGenerator,
) {
  operator fun invoke(model: OrganizationModel): OrganizationRep =
    OrganizationRep(
      guid = model.guid,
      name = model.name,
    )

  operator fun invoke(creator: OrganizationRep.Creator): OrganizationModel.Creator =
    OrganizationModel.Creator(
      guid = guidGenerator.generate(),
      name = creator.name,
    )
}
