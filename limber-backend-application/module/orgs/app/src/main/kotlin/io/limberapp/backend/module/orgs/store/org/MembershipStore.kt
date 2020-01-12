package io.limberapp.backend.module.orgs.store.org

import com.piperframework.store.Store
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import java.util.UUID

internal interface MembershipStore : Store {

    fun create(orgId: UUID, model: MembershipModel)

    fun get(orgId: UUID, userId: UUID): MembershipModel?

    fun delete(orgId: UUID, userId: UUID)
}
