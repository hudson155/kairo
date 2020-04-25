package io.limberapp.backend.module.orgs.testing.fixtures.org

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime

internal object FeatureRepFixtures {
    data class Fixture(
        val creation: () -> FeatureRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> FeatureRep.Complete
    )

    val default = Fixture(
        {
            FeatureRep.Creation("Home", "/home", FeatureRep.Type.HOME)
        },
        { idSeed ->
            FeatureRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = "Home",
                path = "/home",
                type = FeatureRep.Type.HOME,
                isDefaultFeature = true
            )
        }
    )

    val formsFixture = Fixture(
        {
            FeatureRep.Creation("Forms", "/forms", FeatureRep.Type.FORMS)
        },
        { idSeed ->
            FeatureRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = "Forms",
                path = "/forms",
                type = FeatureRep.Type.FORMS,
                isDefaultFeature = false
            )
        }
    )
}
