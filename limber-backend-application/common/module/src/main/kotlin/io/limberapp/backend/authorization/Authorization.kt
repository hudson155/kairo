package io.limberapp.backend.authorization

import com.piperframework.authorization.PiperAuthorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class Authorization : PiperAuthorization<Jwt> {

    private val logger = LoggerFactory.getLogger(Authorization::class.java)

    override fun authorize(principal: Jwt?): Boolean {
        val authorized = authorizeInternal(principal)
        if (authorized) return true
        if (principal?.isSuperuser == true) {
            logger.info("Overriding Authorization access for user with ID ${principal.user.id}.")
            return true
        }
        return false
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

    class HasAccessToFeature(private val featureId: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            featureId ?: return false
            principal.org ?: return false
            return featureId in principal.org.featureIds
        }
    }
}
