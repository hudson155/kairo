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

internal class DeleteFeatureTest : IntegrationTest() {
  @Test
  fun `feature does not exist`() {
    val featureId = "feat_0"

    test {
      shouldBeUnprocessable("Feature does not exist.") {
        featureClient(FeatureApi.Delete(featureId))
      }
    }
  }

  @Test
  fun `feature exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      featureClient(FeatureApi.Delete(feature.id))
        .shouldBe(FeatureFixture.home(organization.id, "feat_0", isDefault = true))
      shouldNotBeFound {
        featureClient(FeatureApi.Get(feature.id))
      }
    }
  }
}
