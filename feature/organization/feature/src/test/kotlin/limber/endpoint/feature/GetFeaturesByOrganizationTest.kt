package limber.endpoint.feature

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import limber.api.feature.FeatureApi
import limber.api.organization.OrganizationApi
import limber.rep.feature.FeatureRep
import limber.rep.organization.OrganizationRep
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
      featureClient(FeatureApi.GetByOrganization(organizationGuid))
        .shouldContainExactlyInAnyOrder(feature0, feature1)
    }
  }
}
