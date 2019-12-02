package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.entity.org.MembershipEntity
import io.limberapp.backend.module.orgs.entity.org.OrgEntity
import java.util.UUID

internal interface MembershipStore : Store<OrgEntity> {

    fun create(orgId: UUID, entity: MembershipEntity): Unit?

    fun delete(orgId: UUID, memberId: UUID): Unit?
}
