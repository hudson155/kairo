package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.MembershipModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.get
import io.limberapp.framework.store.update
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore
) : OrgService {

    override fun create(model: OrgModel.Creation) = orgStore.create(model)

    override fun get(id: UUID) = orgStore.get(id)

    override fun getByMemberId(memberId: UUID) = orgStore.getByMemberId(memberId)

    override fun createMembership(id: UUID, model: MembershipModel.Creation) {
        orgStore.createMembership(id, model)
    }

    override fun update(id: UUID, model: OrgModel.Update) = orgStore.update(id, model)
}
