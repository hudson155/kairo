package io.limberapp.backend.authorization

import com.piperframework.authorization.PiperAuthorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import java.util.UUID

@Suppress("MethodOverloading") // Detekt incorrectly thinks overrides in nested subclasses are method overloads.
abstract class Authorization : PiperAuthorization<Jwt> {

    override fun authorize(principal: Jwt?): Boolean {
        if (principal?.isSuperuser == true) return true
        return authorizeInternal(principal)
    }

    private val Jwt.isSuperuser get() = JwtRole.SUPERUSER in roles

    protected abstract fun authorizeInternal(principal: Jwt?): Boolean

    object Public : Authorization() {
        override fun authorizeInternal(principal: Jwt?) = true
    }

    object AnyJwt : Authorization() {
        override fun authorizeInternal(principal: Jwt?) = principal != null
    }

    class Role(private val role: JwtRole?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            role ?: return false
            return role in principal.roles
        }
    }

    class User(private val userId: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            userId ?: return false
            return principal.user.id == userId
        }
    }

    class OrgMember(private val orgId: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            orgId ?: return false
            return principal.org?.id == orgId
        }
    }
}
