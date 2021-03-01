package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureApi
import io.limberapp.api.org.OrgApi
import io.limberapp.exception.feature.FeatureNameIsNotUnique
import io.limberapp.exception.feature.FeaturePathIsNotUnique
import io.limberapp.exception.feature.FeatureRankIsNotUnique
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.rep.feature.FeatureRepFixtures
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostFeatureTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `org does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectError = OrgNotFound().unprocessable()) {
      featureClient(FeatureApi.Post(FeatureRepFixtures.formsFixture.creation(orgGuid)))
    }
  }

  @Test
  fun `duplicate name`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup { orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation())) }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    setup { featureClient(FeatureApi.Post(FeatureRepFixtures.homeFixture.creation(orgRep.guid))) }

    test(expectError = FeatureNameIsNotUnique()) {
      featureClient(FeatureApi.Post(
          rep = FeatureRepFixtures.formsFixture.creation(orgRep.guid).copy(
              name = homeFeatureRep.name,
          ),
      ))
    }

    test(expectResult = orgRep) { orgClient(OrgApi.Get(orgRep.guid)) }
  }

  @Test
  fun `duplicate path`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup { orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation())) }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    setup { featureClient(FeatureApi.Post(FeatureRepFixtures.homeFixture.creation(orgRep.guid))) }

    test(expectError = FeaturePathIsNotUnique()) {
      featureClient(FeatureApi.Post(
          rep = FeatureRepFixtures.formsFixture.creation(orgRep.guid).copy(
              path = homeFeatureRep.path,
          ),
      ))
    }

    test(expectResult = orgRep) { orgClient(OrgApi.Get(orgRep.guid)) }
  }

  @Test
  fun `duplicate rank`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup { orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation())) }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    setup { featureClient(FeatureApi.Post(FeatureRepFixtures.homeFixture.creation(orgRep.guid))) }

    test(expectError = FeatureRankIsNotUnique()) {
      featureClient(FeatureApi.Post(
          rep = FeatureRepFixtures.formsFixture.creation(orgRep.guid).copy(
              rank = homeFeatureRep.rank,
          ),
      ))
    }

    test(expectResult = orgRep) { orgClient(OrgApi.Get(orgRep.guid)) }
  }

  @Test
  fun `happy path`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup { orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation())) }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    test(expectResult = homeFeatureRep) {
      featureClient(FeatureApi.Post(FeatureRepFixtures.homeFixture.creation(orgRep.guid)))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Post(FeatureRepFixtures.formsFixture.creation(orgRep.guid)))
    }

    test(expectResult = orgRep) { orgClient(OrgApi.Get(orgRep.guid)) }
  }
}
