package io.limberapp.auth.jwt

import io.limberapp.permissions.org.OrgPermissions
import java.util.UUID

data class JwtOrg(
    val guid: UUID,
    val name: String,
    val isOwner: Boolean,
    val permissions: OrgPermissions,
)
