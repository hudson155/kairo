package limber.fixture.organization

import limber.api.organization.OrganizationApi
import limber.rep.organization.OrganizationRep
import limber.testing.IntegrationTest
import java.util.UUID

internal abstract class OrganizationFixture {
  abstract val creator: OrganizationRep.Creator
  abstract operator fun invoke(guid: UUID): OrganizationRep

  internal companion object {
    val acmeCo: OrganizationFixture = object : OrganizationFixture() {
      override val creator: OrganizationRep.Creator =
        OrganizationRep.Creator(name = " Acme Co. ")

      override fun invoke(guid: UUID): OrganizationRep =
        OrganizationRep(guid = guid, name = "Acme Co.")
    }

    val universalExports: OrganizationFixture = object : OrganizationFixture() {
      override val creator: OrganizationRep.Creator =
        OrganizationRep.Creator(name = " Universal Exports ")

      override fun invoke(guid: UUID): OrganizationRep =
        OrganizationRep(guid = guid, name = "Universal Exports")
    }
  }
}

internal suspend fun IntegrationTest.create(fixture: OrganizationFixture): OrganizationRep =
  organizationClient(OrganizationApi.Create(fixture.creator))
