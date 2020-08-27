package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object OrgRepFixtures {
  data class Fixture(
    val creation: (ownerUserGuid: UUID) -> OrgRep.Creation,
    val complete: ResourceTest.(ownerUserGuid: UUID, idSeed: Int) -> OrgRep.Complete,
  )

  val crankyPastaFixture = Fixture({ ownerUserGuid ->
    OrgRep.Creation("Cranky Pasta", ownerUserGuid)
  }, { ownerUserGuid, idSeed ->
    OrgRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      name = "Cranky Pasta",
      ownerUserGuid = ownerUserGuid,
      features = listOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
    )
  })

  val dynamicTennisFixture = Fixture({ ownerUserGuid ->
    OrgRep.Creation("Dynamic Tennis", ownerUserGuid)
  }, { ownerUserGuid, idSeed ->
    OrgRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      name = "Dynamic Tennis",
      ownerUserGuid = ownerUserGuid,
      features = listOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
    )
  })
}
