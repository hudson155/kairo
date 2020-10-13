package io.limberapp.backend.authorization.principal

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import java.util.*

data class JwtOrg(
    val guid: UUID,
    val name: String,
    val isOwner: Boolean,
    val permissions: OrgPermissions,
    val features: Map<UUID, JwtFeature>,
)
