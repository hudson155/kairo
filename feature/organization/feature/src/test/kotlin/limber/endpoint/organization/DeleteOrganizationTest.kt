package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.feature.FeatureApi
import limber.api.organization.OrganizationApi
import limber.api.organizationAuth.OrganizationAuthApi
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.fixture.feature.FeatureFixture
import limber.fixture.feature.create
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class DeleteOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationId = "org_0"

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        organizationClient(OrganizationApi.Delete(organizationId))
      }
    }
  }

  @Test
  fun `organization exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val auth = testSetup("Create auth") {
      create(organization.id, OrganizationAuthFixture.acmeCo)
    }

    val hostname = testSetup("Create hostname") {
      create(organization.id, OrganizationHostnameFixture.fooBarBaz)
    }

    val feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      organizationClient(OrganizationApi.Delete(organization.id))
        .shouldBe(organization)
      shouldNotBeFound {
        organizationClient(OrganizationApi.Get(organization.id))
      }
      shouldNotBeFound {
        featureClient(FeatureApi.Get(feature.id))
      }
      shouldNotBeFound {
        hostnameClient(OrganizationHostnameApi.Get(hostname.id))
      }
      shouldNotBeFound {
        authClient(OrganizationAuthApi.Get(auth.id))
      }
    }
  }
}
