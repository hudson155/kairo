package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.mapper.app.membership.MembershipMapper
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.store.org.MembershipStore
import java.util.UUID

internal class MembershipServiceImpl @Inject constructor(
    private val membershipStore: MembershipStore,
    private val membershipMapper: MembershipMapper
) : MembershipService {

    override fun create(orgId: UUID, model: MembershipModel) {
        val entity = membershipMapper.entity(model)
        membershipStore.create(orgId, entity)
    }

    override fun delete(orgId: UUID, memberId: UUID) {
        membershipStore.delete(orgId, memberId)
    }
}
