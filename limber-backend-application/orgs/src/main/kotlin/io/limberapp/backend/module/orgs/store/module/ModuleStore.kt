package io.limberapp.backend.module.orgs.store.module

import io.limberapp.backend.module.orgs.model.module.ModuleModel
import io.limberapp.framework.store.Store
import java.util.UUID

internal interface ModuleStore :
    Store<ModuleModel.Creation, ModuleModel.Complete, ModuleModel.Update> {

    fun getByOrgId(orgId: UUID): List<ModuleModel.Complete>
}
