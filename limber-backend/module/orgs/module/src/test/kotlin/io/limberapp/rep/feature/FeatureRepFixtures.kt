package io.limberapp.rep.feature

import io.limberapp.testing.integration.IntegrationTest
import java.util.UUID

internal object FeatureRepFixtures {
  data class Fixture(
      val creation: () -> FeatureRep.Creation,
      val complete: IntegrationTest.(orgGuid: UUID, idSeed: Int) -> FeatureRep.Complete,
  )

  val homeFixture: Fixture = Fixture({
    FeatureRep.Creation("Home", "/home", FeatureRep.Type.HOME, 0)
  }, { orgGuid, idSeed ->
    FeatureRep.Complete(
        guid = guids[idSeed],
        orgGuid = orgGuid,
        name = "Home",
        path = "/home",
        type = FeatureRep.Type.HOME,
        rank = 0,
        isDefaultFeature = false,
    )
  })

  val formsFixture: Fixture = Fixture({
    FeatureRep.Creation("Forms", "/forms", FeatureRep.Type.FORMS, 1)
  }, { orgGuid, idSeed ->
    FeatureRep.Complete(
        guid = guids[idSeed],
        orgGuid = orgGuid,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        rank = 1,
        isDefaultFeature = false,
    )
  })
}
