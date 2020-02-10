package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.store.org.MembershipStore
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class MembershipServiceImpl @Inject constructor(
    private val membershipStore: MembershipStore,
    private val orgStore: OrgStore
) : MembershipService by membershipStore {

    override fun create(orgId: UUID, models: Set<MembershipModel>) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        membershipStore.create(orgId, models)
    }

    override fun create(orgId: UUID, model: MembershipModel) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        membershipStore.create(orgId, model)
    }

    override fun delete(orgId: UUID, userId: UUID) {
        orgStore.get(orgId) ?: throw OrgNotFound()
        membershipStore.delete(orgId, userId)
    }
}
