package limber.mapper.organizationHostname

import com.google.inject.Inject
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.util.id.IdGenerator

internal class OrganizationHostnameMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("host")

  operator fun invoke(hostname: OrganizationHostnameModel): OrganizationHostnameRep =
    OrganizationHostnameRep(
      id = hostname.id,
      organizationId = hostname.organizationId,
      hostname = hostname.hostname,
    )

  operator fun invoke(
    organizationId: String,
    creator: OrganizationHostnameRep.Creator,
  ): OrganizationHostnameModel.Creator =
    OrganizationHostnameModel.Creator(
      id = idGenerator.generate(),
      organizationId = organizationId,
      hostname = creator.hostname,
    )
}
