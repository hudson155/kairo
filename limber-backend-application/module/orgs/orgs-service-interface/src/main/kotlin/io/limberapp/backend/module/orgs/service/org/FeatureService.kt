package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.UUID

interface FeatureService {
    fun create(orgGuid: UUID, model: FeatureModel)

    fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update): FeatureModel

    fun delete(orgGuid: UUID, featureGuid: UUID)
}
