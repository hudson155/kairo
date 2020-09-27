package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import java.time.LocalDateTime

internal object FeatureRepFixtures {
  data class Fixture(
    val creation: () -> FeatureRep.Creation,
    val complete: IntegrationTest.(idSeed: Int) -> FeatureRep.Complete,
  )

  val homeFixture = Fixture(
    {
      FeatureRep.Creation(0, "Home", "/home", FeatureRep.Type.HOME)
    },
    { idSeed ->
      FeatureRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        rank = 0,
        name = "Home",
        path = "/home",
        type = FeatureRep.Type.HOME,
        isDefaultFeature = false
      )
    }
  )

  val formsFixture = Fixture(
    {
      FeatureRep.Creation(1, "Forms", "/forms", FeatureRep.Type.FORMS)
    },
    { idSeed ->
      FeatureRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        rank = 1,
        name = "Forms",
        path = "/forms",
        type = FeatureRep.Type.FORMS,
        isDefaultFeature = false
      )
    }
  )
}
