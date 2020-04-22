package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import java.time.LocalDateTime
import java.util.UUID

internal object OrgRepFixtures {

    data class Fixture(
        val creation: (ownerAccountId: UUID) -> OrgRep.Creation,
        val complete: ResourceTest.(ownerAccountId: UUID, idSeed: Int) -> OrgRep.Complete
    )

    val crankyPastaFixture = Fixture({ ownerAccountId ->
        OrgRep.Creation("Cranky Pasta", ownerAccountId)
    }, { ownerAccountId, idSeed ->
        OrgRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            name = "Cranky Pasta",
            ownerAccountId = ownerAccountId,
            features = listOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
        )
    })

    val dynamicTennisFixture = Fixture({ ownerAccountId ->
        OrgRep.Creation("Dynamic Tennis", ownerAccountId)
    }, { ownerAccountId, idSeed ->
        OrgRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            name = "Dynamic Tennis",
            ownerAccountId = ownerAccountId,
            features = listOf(FeatureRepFixtures.default.complete(this, idSeed + 1))
        )
    })
}
