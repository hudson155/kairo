package limber.mapper.organization

import com.google.inject.Inject
import limber.model.organization.OrganizationModel
import limber.rep.organization.OrganizationRep
import limber.util.id.IdGenerator

internal class OrganizationMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("org")

  operator fun invoke(organization: OrganizationModel): OrganizationRep =
    OrganizationRep(
      id = organization.id,
      name = organization.name,
    )

  operator fun invoke(creator: OrganizationRep.Creator): OrganizationModel.Creator =
    OrganizationModel.Creator(
      id = idGenerator.generate(),
      name = creator.name,
    )
}
