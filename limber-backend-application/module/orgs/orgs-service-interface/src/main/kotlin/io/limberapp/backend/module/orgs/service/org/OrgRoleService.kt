package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import java.util.UUID

interface OrgRoleService {
    fun create(orgGuid: UUID, model: OrgRoleModel)

    fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel>
}
