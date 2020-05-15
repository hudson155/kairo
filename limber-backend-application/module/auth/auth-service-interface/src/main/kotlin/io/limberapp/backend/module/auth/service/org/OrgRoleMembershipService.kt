package io.limberapp.backend.module.auth.service.org

import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import java.util.*

interface OrgRoleMembershipService {
  fun create(orgGuid: UUID, model: OrgRoleMembershipModel)

  fun getByOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID): Set<OrgRoleMembershipModel>

  fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID)
}
