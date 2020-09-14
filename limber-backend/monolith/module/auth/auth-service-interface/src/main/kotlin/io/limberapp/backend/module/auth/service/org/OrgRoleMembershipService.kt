package io.limberapp.backend.module.auth.service.org

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Auth
interface OrgRoleMembershipService : Finder<OrgRoleMembershipModel, OrgRoleMembershipFinder> {
  fun create(orgGuid: UUID, model: OrgRoleMembershipModel): OrgRoleMembershipModel

  fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID)
}
