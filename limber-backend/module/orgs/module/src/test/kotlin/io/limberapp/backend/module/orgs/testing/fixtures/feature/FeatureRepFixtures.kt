package io.limberapp.backend.module.orgs.testing.fixtures.feature

import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import java.time.LocalDateTime
import java.util.*

internal object FeatureRepFixtures {
  data class Fixture(
      val creation: () -> FeatureRep.Creation,
      val complete: IntegrationTest.(orgGuid: UUID, idSeed: Int) -> FeatureRep.Complete,
  )

  val homeFixture = Fixture(
      {
        FeatureRep.Creation(0, "Home", "/home", FeatureRep.Type.HOME)
      },
      { orgGuid, idSeed ->
        FeatureRep.Complete(
            guid = uuidGenerator[idSeed],
            createdDate = LocalDateTime.now(clock),
            orgGuid = orgGuid,
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
      { orgGuid, idSeed ->
        FeatureRep.Complete(
            guid = uuidGenerator[idSeed],
            createdDate = LocalDateTime.now(clock),
            orgGuid = orgGuid,
            rank = 1,
            name = "Forms",
            path = "/forms",
            type = FeatureRep.Type.FORMS,
            isDefaultFeature = false
        )
      }
  )
}
