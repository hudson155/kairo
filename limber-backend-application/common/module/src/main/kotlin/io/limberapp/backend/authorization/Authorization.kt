package io.limberapp.backend.authorization

import com.piperframework.authorization.PiperAuthorization
import io.limberapp.backend.authorization.permissions.OrgPermission
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import org.slf4j.LoggerFactory
import java.util.UUID

@Suppress("MethodOverloading")
abstract class Authorization : PiperAuthorization<Jwt> {
  private val logger = LoggerFactory.getLogger(Authorization::class.java)

  override fun authorize(principal: Jwt?): Boolean {
    val authorized = authorizeInternal(principal)
    if (authorized) return true
    if (principal?.isSuperuser == true) {
      if (principal.user == null) error("Cannot override Authorization access for a user with no UUID.")
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

  class Role(private val role: JwtRole) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      return role in principal.roles
    }
  }

  class User(private val userGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      return principal.user?.guid == userGuid
    }
  }

  class OrgMember(private val orgGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      return principal.org?.guid == orgGuid
    }
  }

  class OrgMemberWithPermission(
    private val orgGuid: UUID,
    private val orgPermission: OrgPermission
  ) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      if (principal.org?.guid != orgGuid) return false
      return principal.org.permissions.hasPermission(orgPermission)
    }
  }

  class HasAccessToFeature(private val featureGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      principal.org ?: return false
      return featureGuid in principal.org.featureGuids
    }
  }
}
