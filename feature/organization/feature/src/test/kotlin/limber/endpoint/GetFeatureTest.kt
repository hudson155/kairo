package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.FeatureApi
import limber.api.OrganizationApi
import limber.rep.FeatureRep
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetFeatureTest : IntegrationTest() {
  @Test
  fun `feature does not exist`() {
    val organizationGuid = UUID.randomUUID()

    val featureGuid = UUID.randomUUID()

    test {
      shouldNotBeFound {
        featureClient(FeatureApi.Get(organizationGuid, featureGuid))
      }
    }
  }

  @Test
  fun `feature exists`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient.invoke(OrganizationApi.Create(creator))
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
      featureClient(FeatureApi.Get(organizationGuid, feature.guid))
        .shouldBe(feature)
    }
  }
}
