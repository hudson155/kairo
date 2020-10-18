package io.limberapp.backend.module.auth.model.org

import java.util.*

interface OrgRoleMembershipFinder {
  fun orgGuid(orgGuid: UUID)
  fun orgRoleGuid(orgRoleGuid: UUID)
}
