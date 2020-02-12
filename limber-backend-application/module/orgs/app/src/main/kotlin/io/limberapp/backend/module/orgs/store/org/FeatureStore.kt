package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

internal interface FeatureStore : FeatureService {

    fun create(orgId: UUID, models: Set<FeatureModel>)

    fun get(orgId: UUID, featureId: UUID): FeatureModel?

    fun getByOrgId(orgId: UUID): Set<FeatureModel>
}
