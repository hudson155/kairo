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

internal class ListFeaturesByOrganizationTest : IntegrationTest() {
  @Test
  fun `no features`() {
    val organizationId = "org_0"

    test {
      featureClient(FeatureApi.ListByOrganization(organizationId))
        .shouldBeEmpty()
    }
  }

  @Test
  fun `features exist`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val homeFeature = testSetup("Create feature (Home)") {
      create(organization.id, FeatureFixture.home)
    }

    val myFormsFeature = testSetup("Create feature (My forms)") {
      create(organization.id, FeatureFixture.myForms)
    }

    test {
      featureClient(FeatureApi.ListByOrganization(organization.id))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }
}
