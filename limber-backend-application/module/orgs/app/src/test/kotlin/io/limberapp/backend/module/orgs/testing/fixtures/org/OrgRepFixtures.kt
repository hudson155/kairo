package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import java.time.LocalDateTime

internal object OrgRepFixtures {

    val Creation = listOf(
        OrgRep.Creation("Cranky Pasta"),
        OrgRep.Creation("Discreet Bulb")
    )

    val Complete = Creation.map { rep ->
        fun ResourceTest.(idSeed: Int): OrgRep.Complete {
            return OrgRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = rep.name,
                features = listOf(FeatureRepFixtures.Complete[0](idSeed + 1)),
                members = emptyList()
            )
        }
    }
}
