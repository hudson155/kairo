package limber.fixture.organizationAuth

import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.testing.IntegrationTest

internal abstract class OrganizationAuthFixture {
  abstract val creator: OrganizationAuthRep.Creator
  abstract operator fun invoke(organizationId: String, id: String, auth0OrganizationId: String): OrganizationAuthRep

  internal companion object {
    val acmeCo: OrganizationAuthFixture = object : OrganizationAuthFixture() {
      override val creator: OrganizationAuthRep.Creator =
        OrganizationAuthRep.Creator(auth0OrganizationName = " ACME-CO ")

      override fun invoke(organizationId: String, id: String, auth0OrganizationId: String): OrganizationAuthRep =
        OrganizationAuthRep(
          id = id,
          organizationId = organizationId,
          auth0OrganizationId = auth0OrganizationId,
          auth0OrganizationName = "acme-co",
        )
    }

    val universalExports: OrganizationAuthFixture = object : OrganizationAuthFixture() {
      override val creator: OrganizationAuthRep.Creator =
        OrganizationAuthRep.Creator(auth0OrganizationName = " UNIVERSAL-EXPORTS ")

      override fun invoke(organizationId: String, id: String, auth0OrganizationId: String): OrganizationAuthRep =
        OrganizationAuthRep(
          id = id,
          organizationId = organizationId,
          auth0OrganizationId = auth0OrganizationId,
          auth0OrganizationName = "universal-exports",
        )
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationId: String,
  fixture: OrganizationAuthFixture,
): OrganizationAuthRep =
  authClient(OrganizationAuthApi.Create(organizationId, fixture.creator))
