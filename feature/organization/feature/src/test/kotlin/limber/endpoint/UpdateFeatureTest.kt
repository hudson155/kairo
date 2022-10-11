package limber.endpoint

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import limber.api.FeatureApi
import limber.api.OrganizationApi
import limber.rep.FeatureRep
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldHaveValidationErrors
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
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    val feature = testSetup("Create feature") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
      featureClient.invoke(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true,
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      val updater = FeatureRep.Updater()
      featureClient(FeatureApi.Update(organizationGuid, feature.guid, updater))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organizationGuid, feature.guid))
        .shouldBe(feature)
    }
  }

  @Test
  fun `is default, false`() {
    val organizationGuid = UUID.randomUUID()

    val featureGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.isDefault" to "must be true") {
        val updater = FeatureRep.Updater(isDefault = false)
        featureClient(FeatureApi.Update(organizationGuid, featureGuid, updater))
      }
    }
  }

  @Test
  fun `is default, happy`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    var feature0 = testSetup("Create feature (0)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Forms, rootPath = "/forms")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true, // The first feature should be default.
        type = FeatureRep.Type.Forms,
        rootPath = "/forms",
      )
    }

    var feature1 = testSetup("Create feature (1)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[2],
        isDefault = false, // An additional feature should not be default.
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      val updater = FeatureRep.Updater(isDefault = true)
      feature0 = feature0.copy(isDefault = false)
      feature1 = feature1.copy(isDefault = true)
      featureClient(FeatureApi.Update(organizationGuid, feature1.guid, updater))
        .shouldBe(feature1)
      featureClient(FeatureApi.GetByOrganization(organizationGuid))
        .shouldContainExactlyInAnyOrder(feature0, feature1)
    }
  }

  @Test
  fun `is default, happy, idempotent`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    val feature0 = testSetup("Create feature (0)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Forms, rootPath = "/forms")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true, // The first feature should be default.
        type = FeatureRep.Type.Forms,
        rootPath = "/forms",
      )
    }

    val feature1 = testSetup("Create feature (1)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[2],
        isDefault = false, // An additional feature should not be default.
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      val updater = FeatureRep.Updater(isDefault = true)
      featureClient(FeatureApi.Update(organizationGuid, feature0.guid, updater))
        .shouldBe(feature0)
      featureClient(FeatureApi.GetByOrganization(organizationGuid))
        .shouldContainExactlyInAnyOrder(feature0, feature1)
    }
  }

  @Test
  fun `root path, malformed`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    val feature = testSetup("Create feature") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
      featureClient.invoke(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true,
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      shouldHaveValidationErrors("body.rootPath" to "must be a valid path") {
        val updater = FeatureRep.Updater(rootPath = "/place~holder")
        featureClient(FeatureApi.Update(organizationGuid, feature.guid, updater))
      }
    }
  }

  @Test
  fun `root path, duplicate`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    testSetup("Create feature (0)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Forms, rootPath = "/forms")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true, // The first feature should be default.
        type = FeatureRep.Type.Forms,
        rootPath = "/forms",
      )
    }

    val feature1 = testSetup("Create feature (1)") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
      featureClient(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[2],
        isDefault = false, // An additional feature should not be default.
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      shouldBeConflict("Root path already taken.") {
        // Testing string trimming and case-insensitivity.
        val updater = FeatureRep.Updater(rootPath = " /Forms ")
        featureClient(FeatureApi.Update(organizationGuid, feature1.guid, updater))
      }
    }
  }

  @Test
  fun `root path, happy`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    var feature = testSetup("Create feature") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/Placeholder")
      featureClient.invoke(FeatureApi.Create(organizationGuid, creator))
      return@testSetup FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true,
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder",
      )
    }

    test {
      val updater = FeatureRep.Updater(rootPath = " /Newholder ")
      feature = feature.copy(
        rootPath = "/newholder", // Root path should be trimmed and lowercased.
      )
      featureClient(FeatureApi.Update(organizationGuid, feature.guid, updater))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organizationGuid, feature.guid))
        .shouldBe(feature)
    }
  }
}
