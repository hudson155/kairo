package limber.endpoint.feature

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import limber.api.feature.FeatureApi
import limber.fixture.feature.FeatureFixture
import limber.fixture.feature.create
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.rep.feature.FeatureRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.Optional

internal class UpdateFeatureTest : IntegrationTest() {
  @Test
  fun `feature does not exist`() {
    val featureId = "feat_0"

    test {
      shouldBeUnprocessable("Feature does not exist.") {
        val update = FeatureRep.Update(rootPath = "/newholder")
        featureClient(FeatureApi.Update(featureId, update))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val update = FeatureRep.Update()
      featureClient(FeatureApi.Update(feature.id, update))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }

  @Test
  fun `is default, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var homeFeature = testSetup("Create feature (Home)") {
      create(organization.id, FeatureFixture.home)
    }

    var myFormsFeature = testSetup("Create feature (My forms)") {
      create(organization.id, FeatureFixture.myForms)
    }

    test {
      val update = FeatureRep.Update(isDefault = true)
      homeFeature = homeFeature.copy(isDefault = false)
      myFormsFeature = myFormsFeature.copy(isDefault = true)
      featureClient(FeatureApi.Update(myFormsFeature.id, update))
        .shouldBe(myFormsFeature)
      featureClient(FeatureApi.ListByOrganization(organization.id))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }

  @Test
  fun `is default, happy, idempotent`() {
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
      val update = FeatureRep.Update(isDefault = true)
      featureClient(FeatureApi.Update(homeFeature.id, update))
        .shouldBe(homeFeature)
      featureClient(FeatureApi.ListByOrganization(organization.id))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }

  @Test
  fun `name, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val update = FeatureRep.Update(name = "New name")
      feature = feature.copy(name = "New name")
      featureClient(FeatureApi.Update(feature.id, update))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }

  @Test
  fun `icon name, happy, change`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val update = FeatureRep.Update(iconName = Optional.of(" assignment "))
      feature = feature.copy(iconName = "assignment")
      featureClient(FeatureApi.Update(feature.id, update))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }

  @Test
  fun `icon name, happy, remove`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val update = FeatureRep.Update(iconName = Optional.empty())
      feature = feature.copy(iconName = null)
      featureClient(FeatureApi.Update(feature.id, update))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }

  @Test
  fun `root path, duplicate`() {
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
      shouldBeConflict("Root path already taken.") {
        val update = FeatureRep.Update(rootPath = homeFeature.rootPath)
        featureClient(FeatureApi.Update(myFormsFeature.id, update))
      }
    }
  }

  @Test
  fun `root path, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.id, FeatureFixture.home)
    }

    test {
      val update = FeatureRep.Update(rootPath = "/new-path")
      feature = feature.copy(rootPath = "/new-path")
      featureClient(FeatureApi.Update(feature.id, update))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(feature.id))
        .shouldBe(feature)
    }
  }
}
