package io.limberapp.backend.module.auth.model.org

import java.time.ZonedDateTime
import java.util.*

data class OrgRoleMembershipModel(
    val createdDate: ZonedDateTime,
    val orgRoleGuid: UUID,
    val accountGuid: UUID,
)
