package io.limberapp.backend.module.auth.model.org

import io.limberapp.backend.LimberModule
import java.util.*

@LimberModule.Auth
interface OrgRoleFinder {
  fun orgGuid(orgGuid: UUID)
  fun orgRoleGuid(orgRoleGuid: UUID)
  fun accountGuid(accountGuid: UUID)
}
