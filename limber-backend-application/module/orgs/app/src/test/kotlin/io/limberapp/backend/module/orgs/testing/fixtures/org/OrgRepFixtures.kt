package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.util.defaultFeatureRep
import java.time.LocalDateTime

internal object OrgRepFixtures {

    val Creation = listOf(
        OrgRep.Creation("Cranky Pasta"),
        OrgRep.Creation("Discreet Bulb")
    )

    val Complete = Creation.map<OrgRep.Creation, ResourceTest.(idSeed: Int) -> OrgRep.Complete> {
        { idSeed ->
            OrgRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = it.name,
                features = listOf(defaultFeatureRep(deterministicUuidGenerator[idSeed + 1])),
                members = emptyList()
            )
        }
    }
}
