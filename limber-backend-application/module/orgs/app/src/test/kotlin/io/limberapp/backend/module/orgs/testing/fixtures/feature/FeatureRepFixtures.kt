package io.limberapp.backend.module.orgs.testing.fixtures.feature

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime

internal object FeatureRepFixtures {

    val Creation = listOf(
        FeatureRep.Creation("Home", "/home", FeatureModel.Type.HOME),
        FeatureRep.Creation("Events", "/events", FeatureModel.Type.HOME)
    )

    val Complete = Creation.map { rep ->
        fun ResourceTest.(idSeed: Int): FeatureRep.Complete {
            return FeatureRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = rep.name,
                path = rep.path,
                type = rep.type
            )
        }
    }
}
