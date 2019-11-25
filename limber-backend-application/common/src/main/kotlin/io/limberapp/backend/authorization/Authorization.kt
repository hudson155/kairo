package io.limberapp.backend.authorization

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.Payload
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import com.piperframework.authorization.LimberAuthorization
import com.piperframework.jackson.objectMapper.LimberObjectMapper
import java.util.UUID

@Suppress("MethodOverloading") // Detekt incorrectly thinks overrides in nested subclasses are method overloads.
sealed class Authorization : com.piperframework.authorization.LimberAuthorization {

    private val objectMapper = LimberObjectMapper()

    override fun authorize(payload: Payload?): Boolean {
        val jwt = jwtFromPayload(payload)
        if (jwt?.isSuperuser == true) return true
        return authorizeInternal(jwt)
    }

    private fun jwtFromPayload(payload: Payload?): Jwt? {
        payload ?: return null
        return try {
            Jwt(
                orgs = objectMapper.readValue(payload.getClaim(Claims.orgs).asString()),
                roles = objectMapper.readValue(payload.getClaim(Claims.roles).asString()),
                user = objectMapper.readValue(payload.getClaim(Claims.user).asString())
            )
        } catch (_: JWTDecodeException) {
            null
        }
    }

    private val Jwt.isSuperuser get() = roles.contains(JwtRole.SUPERUSER)

    protected abstract fun authorizeInternal(payload: Jwt?): Boolean

    object Public : Authorization() {
        override fun authorizeInternal(payload: Jwt?) = true
    }

    object AnyJwt : Authorization() {
        override fun authorizeInternal(payload: Jwt?) = payload != null
    }

    object Superuser : Authorization() {
        override fun authorizeInternal(payload: Jwt?) = false
    }

    class User(private val userId: UUID?) : Authorization() {
        override fun authorizeInternal(payload: Jwt?): Boolean {
            payload ?: return false
            userId ?: return false
            return payload.user.id == userId
        }
    }

    class OrgMember(private val orgId: UUID?) : Authorization() {
        override fun authorizeInternal(payload: Jwt?): Boolean {
            payload ?: return false
            orgId ?: return false
            return payload.orgs.containsKey(orgId)
        }
    }
}
