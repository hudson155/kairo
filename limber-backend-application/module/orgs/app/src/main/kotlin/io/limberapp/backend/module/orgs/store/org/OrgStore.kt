package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.entity.org.FeatureEntity
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import java.util.UUID

internal interface OrgStore : Store<OrgEntity> {

    fun create(entity: OrgEntity)

    fun get(id: UUID): OrgEntity?

    fun update(id: UUID, update: OrgEntity.Update): OrgEntity?

    fun getByMemberId(memberId: UUID): List<OrgEntity>

    fun createMembership(id: UUID, entity: MembershipEntity): Unit?

    fun deleteMembership(id: UUID, memberId: UUID): Unit?

    fun createFeature(id: UUID, entity: FeatureEntity): Unit?

    fun deleteFeature(id: UUID, featureId: UUID): Unit?

    fun delete(id: UUID): Unit?
}
