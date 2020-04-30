package io.limberapp.backend.module.orgs.mapper.org

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class FeatureMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {
    fun model(orgGuid: UUID, rep: FeatureRep.Creation) = FeatureModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        orgGuid = orgGuid,
        name = rep.name,
        path = rep.path,
        type = type(rep.type),
        isDefaultFeature = false
    )

    fun completeRep(model: FeatureModel) = FeatureRep.Complete(
        guid = model.guid,
        createdDate = model.createdDate,
        name = model.name,
        path = model.path,
        type = type(model.type),
        isDefaultFeature = model.isDefaultFeature
    )

    fun update(rep: FeatureRep.Update) = FeatureModel.Update(
        name = rep.name,
        path = rep.path,
        isDefaultFeature = rep.isDefaultFeature
    )

    private fun type(type: FeatureRep.Type) = when (type) {
        FeatureRep.Type.FORMS -> FeatureModel.Type.FORMS
        FeatureRep.Type.HOME -> FeatureModel.Type.HOME
    }

    private fun type(type: FeatureModel.Type) = when (type) {
        FeatureModel.Type.FORMS -> FeatureRep.Type.FORMS
        FeatureModel.Type.HOME -> FeatureRep.Type.HOME
    }
}
