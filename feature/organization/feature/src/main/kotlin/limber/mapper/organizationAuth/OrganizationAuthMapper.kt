package limber.mapper.organizationAuth

import com.google.inject.Inject
import limber.exception.organizationAuth.OrganizationAuthIdIsNull
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.util.id.IdGenerator
import java.util.UUID

internal class OrganizationAuthMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("auth")

  operator fun invoke(auth: OrganizationAuthModel): OrganizationAuthRep =
    OrganizationAuthRep(
      id = auth.id,
      organizationGuid = auth.organizationGuid,
      auth0OrganizationId = auth.auth0OrganizationId ?: throw OrganizationAuthIdIsNull(),
      auth0OrganizationName = auth.auth0OrganizationName,
    )

  operator fun invoke(
    organizationGuid: UUID,
    creator: OrganizationAuthRep.Creator,
  ): OrganizationAuthModel.Creator =
    OrganizationAuthModel.Creator(
      id = idGenerator.generate(),
      organizationGuid = organizationGuid,
      auth0OrganizationName = creator.auth0OrganizationName,
    )
}
