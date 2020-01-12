package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.UUID

internal interface FeatureStore : Store {

    fun create(orgId: UUID, model: FeatureModel)

    fun get(orgId: UUID, featureId: UUID): FeatureModel?

    fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update): FeatureModel

    fun delete(orgId: UUID, featureId: UUID)
}
