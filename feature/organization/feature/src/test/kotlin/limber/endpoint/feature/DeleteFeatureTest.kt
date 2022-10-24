package limber.endpoint.feature

import io.kotest.matchers.shouldBe
import limber.api.feature.FeatureApi
import limber.fixture.feature.FeatureFixture
import limber.fixture.feature.create
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFeatureTest : IntegrationTest() {
  @Test
  fun `feature does not exist`() {
    val organizationGuid = UUID.randomUUID()

    val featureGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Feature does not exist.") {
        featureClient(FeatureApi.Delete(organizationGuid, featureGuid))
      }
    }
  }

  @Test
  fun `feature exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      featureClient(FeatureApi.Delete(organization.guid, feature.guid))
        .shouldBe(FeatureFixture.home(organization.guid, guidGenerator[1]).copy(isDefault = true))
      shouldNotBeFound {
        featureClient(FeatureApi.Get(organization.guid, feature.guid))
      }
    }
  }
}
