package io.limberapp.backend.module.orgs.service.module

import com.google.inject.Inject
import io.ktor.features.NotFoundException
import io.limberapp.backend.module.orgs.mapper.module.ModuleMapper
import io.limberapp.backend.module.orgs.model.module.ModuleModel
import io.limberapp.backend.module.orgs.store.module.ModuleStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.getById
import io.limberapp.framework.store.update
import java.util.UUID

internal class ModuleServiceImpl @Inject constructor(
    private val moduleStore: ModuleStore
) : ModuleService {

    override fun create(model: ModuleModel.Creation) = moduleStore.create(model)

    override fun getById(id: UUID) = moduleStore.getById(id)

    override fun getByOrgId(orgId: UUID) = moduleStore.getByOrgId(orgId)

    override fun update(id: UUID, model: ModuleModel.Update): ModuleModel.Complete {
        return moduleStore.update(id, model) ?: throw NotFoundException()
    }
}
