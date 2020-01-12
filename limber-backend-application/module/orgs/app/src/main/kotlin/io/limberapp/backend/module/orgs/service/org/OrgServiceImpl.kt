package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore
) : OrgService {

    override fun create(model: OrgModel) = orgStore.create(model)

    override fun get(orgId: UUID) = orgStore.get(orgId)

    override fun getByMemberId(memberId: UUID) = orgStore.getByMemberId(memberId)

    override fun update(orgId: UUID, update: OrgModel.Update) = orgStore.update(orgId, update)

    override fun delete(orgId: UUID) = orgStore.delete(orgId)
}
