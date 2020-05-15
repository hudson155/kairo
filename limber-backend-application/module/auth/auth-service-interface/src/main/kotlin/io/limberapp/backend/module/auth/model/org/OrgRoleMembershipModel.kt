package io.limberapp.backend.module.auth.model.org

import java.time.LocalDateTime
import java.util.UUID

data class OrgRoleMembershipModel(
  val createdDate: LocalDateTime,
  val orgRoleGuid: UUID,
  val accountGuid: UUID
)
