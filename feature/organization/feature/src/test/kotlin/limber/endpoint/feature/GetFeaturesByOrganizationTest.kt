package limber.endpoint.feature

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import limber.api.feature.FeatureApi
import limber.fixture.feature.FeatureFixture
import limber.fixture.feature.create
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.testing.IntegrationTest
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetFeaturesByOrganizationTest : IntegrationTest() {
  @Test
  fun `no features`() {
    val organizationGuid = UUID.randomUUID()

    test {
      featureClient(FeatureApi.GetByOrganization(organizationGuid))
        .shouldBeEmpty()
    }
  }

  @Test
  fun `features exist`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val homeFeature = testSetup("Create feature (Home)") {
      create(organization.guid, FeatureFixture.home)
    }

    val myFormsFeature = testSetup("Create feature (My forms)") {
      create(organization.guid, FeatureFixture.myForms)
    }

    test {
      featureClient(FeatureApi.GetByOrganization(organization.guid))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }
}
