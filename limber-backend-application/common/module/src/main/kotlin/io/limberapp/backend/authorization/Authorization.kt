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
            logger.info("Overriding Authorization access for user with UUID ${principal.user.guid}.")
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

    class User(private val userGuid: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            userGuid ?: return false
            return principal.user.guid == userGuid
        }
    }

    class OrgMember(private val orgGuid: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            orgGuid ?: return false
            return principal.org?.guid == orgGuid
        }
    }

    class HasAccessToFeature(private val featureGuid: UUID?) : Authorization() {
        override fun authorizeInternal(principal: Jwt?): Boolean {
            principal ?: return false
            featureGuid ?: return false
            principal.org ?: return false
            return featureGuid in principal.org.featureGuids
        }
    }
}
