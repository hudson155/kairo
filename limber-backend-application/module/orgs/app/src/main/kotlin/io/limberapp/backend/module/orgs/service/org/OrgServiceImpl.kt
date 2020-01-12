package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.google.inject.name.Named
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.orgs.model.org.OrgModel
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    @Store private val orgStore: OrgService
) : OrgService {

    override fun create(model: OrgModel) = orgStore.create(model)

    override fun get(orgId: UUID) = orgStore.get(orgId)

    override fun getByMemberId(memberId: UUID) = orgStore.getByMemberId(memberId)

    override fun update(orgId: UUID, update: OrgModel.Update) = orgStore.update(orgId, update)

    override fun delete(orgId: UUID) = orgStore.delete(orgId)
}
