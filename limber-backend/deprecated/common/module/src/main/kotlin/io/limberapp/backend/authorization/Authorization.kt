package io.limberapp.backend.authorization

import io.limberapp.backend.authorization.principal.JwtPrincipal
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.authorization.LimberAuthorization
import io.limberapp.permissions.AccountRole
import io.limberapp.permissions.featurePermissions.FeaturePermission
import io.limberapp.permissions.orgPermissions.OrgPermission
import org.slf4j.LoggerFactory
import java.util.*

@Suppress("MethodOverloading")
abstract class Authorization : LimberAuthorization<JwtPrincipal> {
  private val logger = LoggerFactory.getLogger(Authorization::class.java)

  final override fun authorize(principal: JwtPrincipal?): Boolean {
    val authorized = authorizeInternal(principal?.jwt)
    if (authorized) return true
    if (principal?.canOverride() == true) {
      logger.info("Overriding Authorization access"
          + " for user with ${principal.userGuid?.let { "UUID $it" } ?: "no UUID"}"
          + " and account roles ${principal.jwt.roles}."
      )
      return true
    }
    return false
  }

  private fun JwtPrincipal.canOverride() = overridingRoles.intersect(jwt.roles).isNotEmpty()

  protected abstract fun authorizeInternal(principal: Jwt?): Boolean

  open val overridingRoles = setOf(AccountRole.LIMBER_SERVER, AccountRole.SUPERUSER)

  object Public : Authorization() {
    override fun authorizeInternal(principal: Jwt?) = true
  }

  object AnyJwt : Authorization() {
    override fun authorizeInternal(principal: Jwt?) = principal != null
  }

  class Role(private val role: AccountRole) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      principal ?: return false
      return role in principal.roles
    }

    override val overridingRoles =
        if (role == AccountRole.SUPERUSER) setOf(AccountRole.SUPERUSER) else super.overridingRoles
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

  /**
   * [ignoreOrgGuid] must be true. It's required so the caller considers using [OrgMemberWithPermission] instead. This
   * should only be used for operations that are independent from the org.
   */
  class WithOrgPermission(
      private val orgPermission: OrgPermission,
      private val ignoreOrgGuid: Boolean,
  ) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
      check(ignoreOrgGuid)
      principal ?: return false
      val org = principal.org ?: return false
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
