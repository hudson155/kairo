package io.limberapp.backend.module.orgs.mapper.app.feature

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
import io.limberapp.backend.module.orgs.model.org.FeatureModel

internal class FeatureMapper @Inject constructor() {

    fun entity(model: FeatureModel) = FeatureEntity(
        id = model.id,
        created = model.created,
        name = model.name,
        path = model.path,
        type = model.type
    )

    fun model(entity: FeatureEntity) = FeatureModel(
        id = entity.id,
        created = entity.created,
        name = entity.name,
        path = entity.path,
        type = entity.type
    )

    fun update(model: FeatureModel.Update) = FeatureEntity.Update(
        name = model.name,
        path = model.path
    )
}
