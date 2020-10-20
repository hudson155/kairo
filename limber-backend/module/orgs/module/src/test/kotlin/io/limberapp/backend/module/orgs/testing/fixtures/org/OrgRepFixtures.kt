package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.common.util.time.inUTC
import java.time.ZonedDateTime

internal object OrgRepFixtures {
  data class Fixture(
      val creation: () -> OrgRep.Creation,
      val complete: IntegrationTest.(idSeed: Int) -> OrgRep.Complete,
  )

  val crankyPastaFixture = Fixture({
    OrgRep.Creation("Cranky Pasta")
  }, { idSeed ->
    OrgRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        name = "Cranky Pasta",
        ownerUserGuid = null,
        features = emptyList(),
    )
  })

  val dynamicTennisFixture = Fixture({
    OrgRep.Creation("Dynamic Tennis")
  }, { idSeed ->
    OrgRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = ZonedDateTime.now(clock).inUTC(),
        name = "Dynamic Tennis",
        ownerUserGuid = null,
        features = emptyList(),
    )
  })
}
