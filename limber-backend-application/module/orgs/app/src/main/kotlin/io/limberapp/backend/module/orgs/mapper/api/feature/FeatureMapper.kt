package io.limberapp.backend.module.orgs.mapper.api.feature

import com.google.inject.Inject
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import java.time.Clock
import java.time.LocalDateTime

internal class FeatureMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun defaultModel() = FeatureModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(),
        name = "Home",
        path = "/home",
        type = FeatureModel.Type.HOME
    )

    fun model(rep: FeatureRep.Creation) = FeatureModel(
        id = uuidGenerator.generate(),
        created = LocalDateTime.now(clock),
        name = rep.name,
        path = rep.path,
        type = rep.type
    )

    fun completeRep(model: FeatureModel) = FeatureRep.Complete(
        id = model.id,
        created = model.created,
        name = model.name,
        path = model.path,
        type = model.type
    )

    fun update(rep: FeatureRep.Update) = FeatureModel.Update(
        name = rep.name,
        path = rep.path
    )
}
