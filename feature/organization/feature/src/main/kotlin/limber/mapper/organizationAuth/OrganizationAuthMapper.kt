package limber.mapper.organizationAuth

import com.google.inject.Inject
import limber.exception.organizationAuth.OrganizationAuthIdIsNull
import limber.model.organizationAuth.OrganizationAuthModel
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.util.id.IdGenerator

internal class OrganizationAuthMapper @Inject constructor(
  idGenerator: IdGenerator.Factory,
) {
  private val idGenerator: IdGenerator = idGenerator("auth")

  operator fun invoke(auth: OrganizationAuthModel): OrganizationAuthRep =
    OrganizationAuthRep(
      id = auth.id,
      organizationId = auth.organizationId,
      auth0OrganizationId = auth.auth0OrganizationId ?: throw OrganizationAuthIdIsNull(),
      auth0OrganizationName = auth.auth0OrganizationName,
    )

  operator fun invoke(
    organizationId: String,
    creator: OrganizationAuthRep.Creator,
  ): OrganizationAuthModel.Creator =
    OrganizationAuthModel.Creator(
      id = idGenerator.generate(),
      organizationId = organizationId,
      auth0OrganizationName = creator.auth0OrganizationName,
    )
}
