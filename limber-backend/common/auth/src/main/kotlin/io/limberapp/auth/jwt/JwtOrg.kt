package io.limberapp.auth.jwt

import io.limberapp.permissions.orgPermissions.OrgPermissions
import java.util.*

data class JwtOrg(
    val guid: UUID,
    val name: String,
    val isOwner: Boolean,
    val permissions: OrgPermissions,
    val features: Map<UUID, JwtFeature>,
)
