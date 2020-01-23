package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val featureStore: FeatureStore,
    private val orgStore: OrgStore
) : FeatureService by featureStore {

    override fun create(orgId: UUID, models: List<FeatureModel>) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        featureStore.create(orgId, models)
    }

    override fun create(orgId: UUID, model: FeatureModel) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        featureStore.create(orgId, model)
    }

    override fun update(orgId: UUID, featureId: UUID, update: FeatureModel.Update): FeatureModel {
        orgStore.get(orgId) ?: throw OrgNotFound()
        return featureStore.update(orgId, featureId, update)
    }

    override fun delete(orgId: UUID, featureId: UUID) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        return featureStore.delete(orgId, featureId)
    }
}
