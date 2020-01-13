package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    @Store private val featureStore: FeatureService,
    @Store private val orgStore: OrgService
) : FeatureService by featureStore {

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
