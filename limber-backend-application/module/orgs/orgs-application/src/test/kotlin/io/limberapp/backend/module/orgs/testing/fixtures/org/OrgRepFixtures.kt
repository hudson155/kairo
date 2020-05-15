package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object OrgRepFixtures {
  data class Fixture(
    val creation: (ownerAccountGuid: UUID) -> OrgRep.Creation,
    val complete: ResourceTest.(ownerAccountGuid: UUID, idSeed: Int) -> OrgRep.Complete
  )

  val crankyPastaFixture = Fixture({ ownerAccountGuid ->
    OrgRep.Creation("Cranky Pasta", ownerAccountGuid)
  }, { ownerAccountGuid, idSeed ->
    OrgRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      name = "Cranky Pasta",
      ownerAccountGuid = ownerAccountGuid,
      features = setOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
    )
  })

  val dynamicTennisFixture = Fixture({ ownerAccountGuid ->
    OrgRep.Creation("Dynamic Tennis", ownerAccountGuid)
  }, { ownerAccountGuid, idSeed ->
    OrgRep.Complete(
      guid = deterministicUuidGenerator[idSeed],
      createdDate = LocalDateTime.now(fixedClock),
      name = "Dynamic Tennis",
      ownerAccountGuid = ownerAccountGuid,
      features = setOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
    )
  })
}
