package limber.fixture.organizationAuth

import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.testing.IntegrationTest
import java.util.UUID

internal abstract class OrganizationAuthFixture {
  abstract val creator: OrganizationAuthRep.Creator
  abstract operator fun invoke(organizationGuid: UUID, id: String, auth0OrganizationId: UUID): OrganizationAuthRep

  internal companion object {
    val acmeCo: OrganizationAuthFixture = object : OrganizationAuthFixture() {
      override val creator: OrganizationAuthRep.Creator =
        OrganizationAuthRep.Creator(auth0OrganizationName = " ACME-CO ")

      override fun invoke(organizationGuid: UUID, id: String, auth0OrganizationId: UUID): OrganizationAuthRep =
        OrganizationAuthRep(
          id = id,
          organizationGuid = organizationGuid,
          auth0OrganizationId = auth0OrganizationId.toString(),
          auth0OrganizationName = "acme-co",
        )
    }

    val universalExports: OrganizationAuthFixture = object : OrganizationAuthFixture() {
      override val creator: OrganizationAuthRep.Creator =
        OrganizationAuthRep.Creator(auth0OrganizationName = " UNIVERSAL-EXPORTS ")

      override fun invoke(organizationGuid: UUID, id: String, auth0OrganizationId: UUID): OrganizationAuthRep =
        OrganizationAuthRep(
          id = id,
          organizationGuid = organizationGuid,
          auth0OrganizationId = auth0OrganizationId.toString(),
          auth0OrganizationName = "universal-exports",
        )
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationGuid: UUID,
  fixture: OrganizationAuthFixture,
): OrganizationAuthRep =
  authClient(OrganizationAuthApi.Create(organizationGuid, fixture.creator))
