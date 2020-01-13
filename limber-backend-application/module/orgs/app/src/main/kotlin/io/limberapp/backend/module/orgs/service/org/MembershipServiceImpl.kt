package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import java.util.UUID

internal class MembershipServiceImpl @Inject constructor(
    @Store private val membershipStore: MembershipService,
    @Store private val orgStore: OrgService
) : MembershipService by membershipStore {

    override fun create(orgId: UUID, model: MembershipModel) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        membershipStore.create(orgId, model)
    }

    override fun delete(orgId: UUID, userId: UUID) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        membershipStore.delete(orgId, userId)
    }
}
