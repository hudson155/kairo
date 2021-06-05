package io.limberapp.auth.jwt

import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.permissions.limber.LimberPermissions
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
