package limber.endpoint.feature

import io.kotest.matchers.shouldBe
import limber.api.feature.FeatureApi
import limber.fixture.feature.FeatureFixture
import limber.fixture.feature.create
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class CreateFeatureTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationId = "org_0"

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = FeatureFixture.home.creator
        featureClient(FeatureApi.Create(organizationId, creator))
      }
    }
  }

  @Test
  fun `root path, duplicate`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      shouldBeConflict("Root path already taken.") {
        val creator = FeatureFixture.myForms.creator.copy(rootPath = feature.rootPath)
        featureClient(FeatureApi.Create(organization.id, creator))
      }
    }
  }

  @Test
  fun `happy, first feature`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val creator = FeatureFixture.home.creator
      val feature = featureClient(FeatureApi.Create(organization.id, creator))
      feature.shouldBe(FeatureFixture.home(organization.id, "feat_0", isDefault = true))
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }

  @Test
  fun `happy, additional feature`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val creator = FeatureFixture.myForms.creator
      val feature = featureClient(FeatureApi.Create(organization.id, creator))
      feature.shouldBe(FeatureFixture.myForms(organization.id, "feat_1"))
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }
}
