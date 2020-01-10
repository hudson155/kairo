package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import java.util.UUID

internal interface FeatureStore : Store<OrgEntity> {

    fun create(orgId: UUID, entity: FeatureEntity)

    fun get(orgId: UUID, featureId: UUID): FeatureEntity?

    fun update(orgId: UUID, featureId: UUID, update: FeatureEntity.Update): FeatureEntity

    fun delete(orgId: UUID, featureId: UUID)
}
