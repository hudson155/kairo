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
      featureClient(FeatureApi.Post(orgGuid, FeatureRepFixtures.formsFixture.creation()))
    }
  }

  @Test
  fun `duplicate name`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(
        features = listOf(FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)),
    )
    setup {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.homeFixture.creation(),
      ))
    }

    test(expectError = FeatureNameIsNotUnique()) {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.formsFixture.creation().copy(
              name = FeatureRepFixtures.homeFixture.creation().name,
          ),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `duplicate path`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(
        features = listOf(FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)),
    )
    setup {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.homeFixture.creation(),
      ))
    }

    test(expectError = FeaturePathIsNotUnique()) {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.formsFixture.creation().copy(
              path = FeatureRepFixtures.homeFixture.creation().path,
          ),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `duplicate rank`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(
        features = listOf(FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)),
    )
    setup {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.homeFixture.creation(),
      ))
    }

    test(expectError = FeatureRankIsNotUnique()) {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.formsFixture.creation().copy(
              rank = FeatureRepFixtures.homeFixture.creation().rank,
          ),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `happy path`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    test(expectResult = homeFeatureRep) {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
