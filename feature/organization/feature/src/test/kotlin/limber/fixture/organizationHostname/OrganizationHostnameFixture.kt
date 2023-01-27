package limber.fixture.organizationHostname

import limber.api.organizationHostname.OrganizationHostnameApi
import limber.rep.organizationHostname.OrganizationHostnameRep
import limber.testing.IntegrationTest
import java.util.UUID

internal abstract class OrganizationHostnameFixture {
  abstract val creator: OrganizationHostnameRep.Creator
  abstract operator fun invoke(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep

  internal companion object {
    val fooBarBaz: OrganizationHostnameFixture = object : OrganizationHostnameFixture() {
      override val creator: OrganizationHostnameRep.Creator =
        OrganizationHostnameRep.Creator(hostname = " foo.BAR.baz ")

      override fun invoke(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep =
        OrganizationHostnameRep(guid = guid, organizationGuid = organizationGuid, hostname = "foo.bar.baz")
    }

    val barBazQux: OrganizationHostnameFixture = object : OrganizationHostnameFixture() {
      override val creator: OrganizationHostnameRep.Creator =
        OrganizationHostnameRep.Creator(hostname = " bar.BAZ.qux ")

      override fun invoke(organizationGuid: UUID, guid: UUID): OrganizationHostnameRep =
        OrganizationHostnameRep(guid = guid, organizationGuid = organizationGuid, hostname = "foo.bar.baz")
    }
  }
}

internal suspend fun IntegrationTest.create(
  organizationGuid: UUID,
  fixture: OrganizationHostnameFixture,
): OrganizationHostnameRep =
  hostnameClient(OrganizationHostnameApi.Create(organizationGuid, fixture.creator))
