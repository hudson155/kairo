package io.limberapp.backend.module.orgs.testing.fixtures.feature

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime

internal object FeatureRepFixtures {

    data class Fixture(
        val creation: () -> FeatureRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> FeatureRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    val default = Fixture(
        {
            FeatureRep.Creation("Home", "/home", FeatureModel.Type.HOME)
        },
        { idSeed ->
            FeatureRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                name = "Home",
                path = "/home",
                type = FeatureModel.Type.HOME
            )
        }
    )

    private val fixtures = listOf(
        Fixture(
            {
                FeatureRep.Creation("Forms", "/forms", FeatureModel.Type.FORMS)
            },
            { idSeed ->
                FeatureRep.Complete(
                    id = deterministicUuidGenerator[idSeed],
                    created = LocalDateTime.now(fixedClock),
                    name = "Forms",
                    path = "/forms",
                    type = FeatureModel.Type.FORMS
                )
            }
        )
    )
}
