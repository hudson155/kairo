package limber.fixture.organization

import limber.api.organization.OrganizationApi
import limber.rep.organization.OrganizationRep
import limber.testing.IntegrationTest

internal abstract class OrganizationFixture {
  abstract val creator: OrganizationRep.Creator
  abstract operator fun invoke(id: String): OrganizationRep

  internal companion object {
    val acmeCo: OrganizationFixture = object : OrganizationFixture() {
      override val creator: OrganizationRep.Creator =
        OrganizationRep.Creator(name = " Acme Co. ")

      override fun invoke(id: String): OrganizationRep =
        OrganizationRep(id = id, name = "Acme Co.")
    }

    val universalExports: OrganizationFixture = object : OrganizationFixture() {
      override val creator: OrganizationRep.Creator =
        OrganizationRep.Creator(name = " Universal Exports ")

      override fun invoke(id: String): OrganizationRep =
        OrganizationRep(id = id, name = "Universal Exports")
    }
  }
}

internal suspend fun IntegrationTest.create(fixture: OrganizationFixture): OrganizationRep =
  organizationClient(OrganizationApi.Create(fixture.creator))
