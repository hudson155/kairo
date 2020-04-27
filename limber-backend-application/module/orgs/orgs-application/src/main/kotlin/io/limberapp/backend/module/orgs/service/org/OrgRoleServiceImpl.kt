package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import io.limberapp.backend.module.orgs.store.org.OrgRoleStore
import java.util.UUID

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
    override fun create(orgGuid: UUID, model: OrgRoleModel) = orgRoleStore.create(orgGuid, model)
}
