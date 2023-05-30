package limber.mapper.organizationHostname

import com.google.inject.Inject
import limber.model.organizationHostname.OrganizationHostnameModel
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.util.id.IdGenerator
import java.util.UUID

internal class OrganizationHostnameMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("host")

  operator fun invoke(hostname: OrganizationHostnameModel): OrganizationHostnameRep =
    OrganizationHostnameRep(
      id = hostname.id,
      organizationGuid = hostname.organizationGuid,
      hostname = hostname.hostname,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: OrganizationHostnameRep.Creator,
  ): OrganizationHostnameModel.Creator =
    OrganizationHostnameModel.Creator(
      id = idGenerator.generate(),
      organizationGuid = organizationGuid,
      hostname = creator.hostname,
    )
}
