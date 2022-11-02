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
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class CreateFeatureTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = FeatureFixture.home.creator
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `name, too short`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 31") {
        val creator = FeatureFixture.home.creator.copy(name = " Ho ")
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `name, too long`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 31") {
        val creator = FeatureFixture.home.creator.copy(name = "A".repeat(32))
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `root path, malformed`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.rootPath" to "must be a valid feature path") {
        val creator = FeatureFixture.home.creator.copy(rootPath = "/place~holder")
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `root path, duplicate`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val feature = testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      shouldBeConflict("Root path already taken.") {
        val creator = FeatureFixture.myForms.creator.copy(rootPath = feature.rootPath)
        featureClient(FeatureApi.Create(organization.guid, creator))
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
      val feature = featureClient(FeatureApi.Create(organization.guid, creator))
      feature.shouldBe(FeatureFixture.home(organization.guid, guidGenerator[1]).copy(isDefault = true))
      featureClient(FeatureApi.Get(feature.guid))
        .shouldBe(feature)
    }
  }

  @Test
  fun `happy, additional feature`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create feature") {
      create(organization.guid, FeatureFixture.home)
    }

    test {
      val creator = FeatureFixture.myForms.creator
      val feature = featureClient(FeatureApi.Create(organization.guid, creator))
      feature.shouldBe(FeatureFixture.myForms(organization.guid, guidGenerator[2]))
      featureClient(FeatureApi.Get(feature.guid))
        .shouldBe(feature)
    }
  }
}
