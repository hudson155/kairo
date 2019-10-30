package io.limberapp.backend.module.orgs.service.module

import io.limberapp.backend.module.orgs.model.module.ModuleModel
import java.util.UUID

internal interface ModuleService {

    fun create(model: ModuleModel.Creation): ModuleModel.Complete

    fun getById(id: UUID): ModuleModel.Complete?

    fun getByOrgId(orgId: UUID): List<ModuleModel.Complete>

    fun update(id: UUID, model: ModuleModel.Update): ModuleModel.Complete
}
