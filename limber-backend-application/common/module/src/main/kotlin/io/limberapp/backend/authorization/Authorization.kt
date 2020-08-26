package io.limberapp.backend.authorization

import com.piperframework.authorization.PiperAuthorization
import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermission
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import org.slf4j.LoggerFactory
import java.util.*

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

  class User(private val userGuid: UUID?) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      userGuid ?: return false
      val user = principal.user ?: return false
      return user.guid == userGuid
    }
  }

  class OrgMember(private val orgGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      return org.guid == orgGuid
    }
  }

  class OrgOwner(private val orgGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      if (org.guid != orgGuid) return false
      return org.isOwner
    }
  }

  class OrgMemberWithPermission(
    private val orgGuid: UUID,
    private val orgPermission: OrgPermission,
  ) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      if (org.guid != orgGuid) return false
      return orgPermission in org.permissions
    }
  }

  class FeatureMember(private val featureGuid: UUID) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      return featureGuid in org.features
    }
  }

  class FeatureMemberWithOrgPermission(
    private val featureGuid: UUID,
    private val orgPermission: OrgPermission,
  ) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      org.features[featureGuid] ?: return false
      // After making sure the feature exists on the org, we blindly check the org permission without checking the org
      // guid. This is ok because features are owned by orgs and users are members of a single org, so if the user has
      // access to the feature, the org GUID must be correct.
      return orgPermission in org.permissions
    }
  }

  class FeatureMemberWithFeaturePermission(
    private val featureGuid: UUID,
    private val featurePermission: FeaturePermission,
  ) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      val org = principal.org ?: return false
      val feature = org.features[featureGuid] ?: return false
      return featurePermission in feature.permissions
    }
  }
}
