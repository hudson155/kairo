package io.limberapp.rep.org

import io.limberapp.testing.integration.IntegrationTest

internal object OrgRepFixtures {
  data class Fixture(
      val creation: () -> OrgRep.Creation,
      val complete: IntegrationTest.(idSeed: Int) -> OrgRep.Complete,
  )

  val crankyPastaFixture: Fixture = Fixture({
    OrgRep.Creation("Cranky Pasta")
  }, { idSeed ->
    OrgRep.Complete(
        guid = guids[idSeed],
        name = "Cranky Pasta",
        ownerUserGuid = null,
        features = emptyList(),
    )
  })

  val dynamicTennisFixture: Fixture = Fixture({
    OrgRep.Creation("Dynamic Tennis")
  }, { idSeed ->
    OrgRep.Complete(
        guid = guids[idSeed],
        name = "Dynamic Tennis",
        ownerUserGuid = null,
        features = emptyList(),
    )
  })
}
