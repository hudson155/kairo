package io.limberapp.common.auth.jwt

import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import java.util.UUID

data class JwtOrg(
    val guid: UUID,
    val name: String,
    val isOwner: Boolean,
    val permissions: OrgPermissions,
)
