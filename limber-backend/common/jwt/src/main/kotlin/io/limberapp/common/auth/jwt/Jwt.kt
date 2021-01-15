package io.limberapp.common.auth.jwt

import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import java.util.UUID

data class Jwt(
    @JsonProperty(JwtClaims.permissions)
    val permissions: LimberPermissions,
    @JsonProperty(JwtClaims.org)
    val org: JwtOrg?,
    @JsonProperty(JwtClaims.features)
    val features: Map<UUID, JwtFeature>?,
    @JsonProperty(JwtClaims.user)
    val user: JwtUser?,
)
