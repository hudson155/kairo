package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class FeatureServiceImpl @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val featureStore: FeatureStore
) : FeatureService {
    override fun createDefaults(orgGuid: UUID): Set<FeatureModel> {
        require(featureStore.getByOrgGuid(orgGuid).isEmpty())
        val feature = FeatureModel(
            guid = uuidGenerator.generate(),
            createdDate = LocalDateTime.now(clock),
            name = "Home",
            path = "/home",
            type = FeatureModel.Type.HOME,
            isDefaultFeature = true
        )
        featureStore.create(orgGuid, feature)
        return setOf(feature)
    }

    override fun create(orgGuid: UUID, model: FeatureModel) = featureStore.create(orgGuid, model)

    override fun getByOrgGuid(orgGuid: UUID) = featureStore.getByOrgGuid(orgGuid)

    override fun update(orgGuid: UUID, featureGuid: UUID, update: FeatureModel.Update) =
        featureStore.update(orgGuid, featureGuid, update)

    override fun delete(orgGuid: UUID, featureGuid: UUID) = featureStore.delete(orgGuid, featureGuid)
}
