package limber.fixture.organizationHostname

import limber.api.organizationHostname.OrganizationHostnameApi
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.testing.IntegrationTest

internal abstract class OrganizationHostnameFixture {
  abstract val creator: OrganizationHostnameRep.Creator
  abstract operator fun invoke(organizationId: String, id: String): OrganizationHostnameRep

  internal companion object {
    val fooBarBaz: OrganizationHostnameFixture = object : OrganizationHostnameFixture() {
      override val creator: OrganizationHostnameRep.Creator =
        OrganizationHostnameRep.Creator(hostname = " foo.BAR.baz ")

      override fun invoke(organizationId: String, id: String): OrganizationHostnameRep =
        OrganizationHostnameRep(id = id, organizationId = organizationId, hostname = "foo.bar.baz")
    }

    val barBazQux: OrganizationHostnameFixture = object : OrganizationHostnameFixture() {
      override val creator: OrganizationHostnameRep.Creator =
        OrganizationHostnameRep.Creator(hostname = " bar.BAZ.qux ")

      override fun invoke(organizationId: String, id: String): OrganizationHostnameRep =
        OrganizationHostnameRep(id = id, organizationId = organizationId, hostname = "foo.bar.baz")
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationId: String,
  fixture: OrganizationHostnameFixture,
): OrganizationHostnameRep =
  hostnameClient(OrganizationHostnameApi.Create(organizationId, fixture.creator))
