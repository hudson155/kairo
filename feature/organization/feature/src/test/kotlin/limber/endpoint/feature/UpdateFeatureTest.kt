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
import java.util.UUID

internal class UpdateFeatureTest : IntegrationTest() {
  @Test
  fun `feature does not exist`() {
    val organizationGuid = UUID.randomUUID()

    val featureGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Feature does not exist.") {
        val updater = FeatureRep.Updater(rootPath = "/newholder")
        featureClient(FeatureApi.Update(organizationGuid, featureGuid, updater))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      val updater = FeatureRep.Updater()
      featureClient(FeatureApi.Update(organization.guid, feature.guid, updater))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organization.guid, feature.guid))
        .shouldBe(feature)
    }
  }

  @Test
  fun `is default, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var homeFeature = testSetup("Create feature (Home)") {
      create(organization.guid, FeatureFixture.home)
    }

    var myFormsFeature = testSetup("Create feature (My forms)") {
      create(organization.guid, FeatureFixture.myForms)
    }

    test {
      val updater = FeatureRep.Updater(isDefault = true)
      homeFeature = homeFeature.copy(isDefault = false)
      myFormsFeature = myFormsFeature.copy(isDefault = true)
      featureClient(FeatureApi.Update(organization.guid, myFormsFeature.guid, updater))
        .shouldBe(myFormsFeature)
      featureClient(FeatureApi.GetByOrganization(organization.guid))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }

  @Test
  fun `is default, happy, idempotent`() {
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
      val updater = FeatureRep.Updater(isDefault = true)
      featureClient(FeatureApi.Update(organization.guid, homeFeature.guid, updater))
        .shouldBe(homeFeature)
      featureClient(FeatureApi.GetByOrganization(organization.guid))
        .shouldContainExactlyInAnyOrder(homeFeature, myFormsFeature)
    }
  }

  @Test
  fun `name, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      val updater = FeatureRep.Updater(name = "New name")
      feature = feature.copy(name = "New name")
      featureClient(FeatureApi.Update(organization.guid, feature.guid, updater))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organization.guid, feature.guid))
        .shouldBe(feature)
    }
  }

  @Test
  fun `root path, duplicate`() {
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
      shouldBeConflict("Root path already taken.") {
        val updater = FeatureRep.Updater(rootPath = homeFeature.rootPath)
        featureClient(FeatureApi.Update(organization.guid, myFormsFeature.guid, updater))
      }
    }
  }

  @Test
  fun `root path, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var feature = testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      val updater = FeatureRep.Updater(rootPath = "/new-path")
      feature = feature.copy(rootPath = "/new-path")
      featureClient(FeatureApi.Update(organization.guid, feature.guid, updater))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organization.guid, feature.guid))
        .shouldBe(feature)
    }
  }
}
