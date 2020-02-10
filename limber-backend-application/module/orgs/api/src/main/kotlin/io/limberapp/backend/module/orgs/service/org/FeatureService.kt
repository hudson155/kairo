package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.UUID

interface FeatureService {

    fun create(orgId: UUID, models: Set<FeatureModel>)

    fun create(orgId: UUID, model: FeatureModel)

    fun get(orgId: UUID, featureId: UUID): FeatureModel?

    fun getByOrgId(orgId: UUID): Set<FeatureModel>

    fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update): FeatureModel

    fun delete(orgId: UUID, featureId: UUID)
}
