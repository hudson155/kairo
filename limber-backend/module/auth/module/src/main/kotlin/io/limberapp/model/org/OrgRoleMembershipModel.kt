package io.limberapp.model.org

import java.time.ZonedDateTime
import java.util.UUID

data class OrgRoleMembershipModel(
    val createdDate: ZonedDateTime,
    val orgRoleGuid: UUID,
    val userGuid: UUID,
)
