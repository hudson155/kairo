package io.limberapp.backend.module.orgs.model.org

import io.limberapp.backend.authorization.permissions.OrgPermissions
import java.time.LocalDateTime
import java.util.UUID

data class OrgRoleModel(
    val guid: UUID,
    val createdDate: LocalDateTime,
    val name: String,
    val permissions: OrgPermissions
)
