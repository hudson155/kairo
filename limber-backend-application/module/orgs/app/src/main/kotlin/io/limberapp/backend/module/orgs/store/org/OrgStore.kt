package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import java.util.UUID

internal interface OrgStore : Store<OrgEntity> {

    fun create(entity: OrgEntity)

    fun get(id: UUID): OrgEntity?

    fun update(id: UUID, update: OrgEntity.Update): OrgEntity?

    fun getByMemberId(memberId: UUID): List<OrgEntity>

    fun delete(id: UUID): Unit?
}
