package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

internal interface FeatureStore : FeatureService {
    fun create(orgGuid: UUID, models: Set<FeatureModel>)

    fun get(orgGuid: UUID, featureGuid: UUID): FeatureModel?

    fun getByOrgGuid(orgGuid: UUID): Set<FeatureModel>
}
