package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import java.util.UUID

internal class MembershipServiceImpl @Inject constructor(
    @Store private val membershipStore: MembershipService
) : MembershipService {

    override fun create(orgId: UUID, model: MembershipModel) = membershipStore.create(orgId, model)

    override fun get(orgId: UUID, userId: UUID) = membershipStore.get(orgId, userId)

    override fun delete(orgId: UUID, userId: UUID) = membershipStore.delete(orgId, userId)
}
