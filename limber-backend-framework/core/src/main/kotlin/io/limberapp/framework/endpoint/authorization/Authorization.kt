package io.limberapp.framework.endpoint.authorization

import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtRole
import java.util.UUID

// Detekt incorrectly thinks overrides in nested subclasses are method overloads.
@Suppress("MethodOverloading")
sealed class Authorization {

    fun authorize(payload: Jwt?): Boolean {
        if (payload?.isSuperuser == true) return true
        return authorizeInternal(payload)
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
