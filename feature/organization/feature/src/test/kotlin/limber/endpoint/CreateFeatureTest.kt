package limber.endpoint

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

internal class CreateFeatureTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `root path, malformed`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.rootPath" to "must be a valid path") {
        val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/place~holder")
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `root path, duplicate`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient.invoke(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    testSetup("Create feature") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Forms, rootPath = "/placeholder")
      featureClient(FeatureApi.Create(organizationGuid, creator))
    }

    test {
      shouldBeConflict("Root path already taken.") {
        val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = "/placeholder")
        featureClient(FeatureApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `happy, first feature`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient.invoke(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    test {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = " /Placeholder ")
      val feature = FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        isDefault = true, // The first feature should be default.
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder", // Root path should be trimmed and lowercased.
      )
      featureClient(FeatureApi.Create(organizationGuid, creator))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organizationGuid, feature.guid))
        .shouldBe(feature)
    }
  }

  @Test
  fun `happy, additional feature`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient.invoke(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    testSetup("Create feature") {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Forms, rootPath = "/forms")
      featureClient(FeatureApi.Create(organizationGuid, creator))
    }

    test {
      val creator = FeatureRep.Creator(type = FeatureRep.Type.Placeholder, rootPath = " /Placeholder ")
      val feature = FeatureRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[2],
        isDefault = false, // An additional feature should not be default.
        type = FeatureRep.Type.Placeholder,
        rootPath = "/placeholder", // Root path should be trimmed and lowercased.
      )
      featureClient(FeatureApi.Create(organizationGuid, creator))
        .shouldBe(feature)
      featureClient(FeatureApi.Get(organizationGuid, feature.guid))
        .shouldBe(feature)
    }
  }
}
