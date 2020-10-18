package io.limberapp.backend.module.auth.model.org

import java.util.*

interface OrgRoleFinder {
  fun orgGuid(orgGuid: UUID)
  fun orgRoleGuid(orgRoleGuid: UUID)
  fun accountGuid(accountGuid: UUID)
}
