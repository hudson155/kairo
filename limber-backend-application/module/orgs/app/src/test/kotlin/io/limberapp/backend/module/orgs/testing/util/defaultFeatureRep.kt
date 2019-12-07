package io.limberapp.backend.module.orgs.testing.util

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal fun ResourceTest.defaultFeatureRep(id: UUID) = FeatureRep.Complete(
    id = id,
    created = LocalDateTime.now(fixedClock),
    name = "Home",
    path = "/home",
    type = FeatureModel.Type.HOME
)
