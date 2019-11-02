package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.ktor.features.NotFoundException
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.getById
import io.limberapp.framework.store.update
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore
) : OrgService {

    override fun create(model: OrgModel.Creation) = orgStore.create(model)

    override fun getById(id: UUID) = orgStore.getById(id)

    override fun getByMemberId(memberId: UUID) = orgStore.getByMemberId(memberId)

    override fun update(id: UUID, model: OrgModel.Update): OrgModel.Complete {
        return orgStore.update(id, model) ?: throw NotFoundException()
    }
}
