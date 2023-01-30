package limber.fixture.organizationAuth

import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.testing.IntegrationTest
import java.util.UUID

internal abstract class OrganizationAuthFixture {
  abstract val creator: OrganizationAuthRep.Creator
  abstract operator fun invoke(organizationGuid: UUID, guid: UUID): OrganizationAuthRep

  internal companion object {
    val acmeCo: OrganizationAuthFixture = object : OrganizationAuthFixture() {
      override val creator: OrganizationAuthRep.Creator =
        OrganizationAuthRep.Creator(auth0OrganizationId = " org_abcdefghijklmnop ")

      override fun invoke(organizationGuid: UUID, guid: UUID): OrganizationAuthRep =
        OrganizationAuthRep(
          guid = guid,
          organizationGuid = organizationGuid,
          auth0OrganizationId = "org_abcdefghijklmnop",
        )
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationGuid: UUID,
  fixture: OrganizationAuthFixture,
): OrganizationAuthRep =
  authClient(OrganizationAuthApi.Set(organizationGuid, fixture.creator))
