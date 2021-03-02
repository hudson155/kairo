package io.limberapp.service.org

import io.limberapp.model.org.OrgRoleMembershipModel
import java.util.UUID

interface OrgRoleMembershipService {
  fun create(model: OrgRoleMembershipModel): OrgRoleMembershipModel

  fun getByOrgRoleGuid(orgRoleGuid: UUID): Set<OrgRoleMembershipModel>

  fun delete(orgRoleGuid: UUID, userGuid: UUID)
}
