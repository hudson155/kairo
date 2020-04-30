package io.limberapp.backend.module.orgs.service.org

import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import java.util.UUID

interface OrgRoleService {
    fun create(model: OrgRoleModel)

    fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel>

    fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel

    fun delete(orgGuid: UUID, orgRoleGuid: UUID)
}
