package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import java.util.UUID

internal interface MembershipStore : Store {

    fun create(orgId: UUID, entity: MembershipEntity)

    fun get(orgId: UUID, userId: UUID): MembershipEntity?

    fun delete(orgId: UUID, userId: UUID)
}
