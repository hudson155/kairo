package io.limberapp.service.org

import io.limberapp.model.org.OrgRoleModel
import java.util.UUID

interface OrgRoleService {
  fun create(model: OrgRoleModel): OrgRoleModel

  operator fun get(orgRoleGuid: UUID): OrgRoleModel?

  fun getByUserGuid(orgGuid: UUID, userGuid: UUID): Set<OrgRoleModel>

  fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel>

  fun update(orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel

  fun delete(orgRoleGuid: UUID)
}
