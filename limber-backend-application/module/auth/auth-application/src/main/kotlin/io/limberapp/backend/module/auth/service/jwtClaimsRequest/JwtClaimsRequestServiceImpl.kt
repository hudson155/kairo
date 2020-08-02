package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import com.piperframework.serialization.Json
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.LimberModule
import io.limberapp.backend.authorization.permissions.featurePermissions.FeaturePermissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.home.HomeFeaturePermissions
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions.Companion.union
import io.limberapp.backend.authorization.principal.JwtFeature
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

private val ORG_OWNER_ORG_ROLE =
  OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS))

internal class JwtClaimsRequestServiceImpl @Inject constructor(
  @OptIn(LimberModule.Orgs::class) private val featureService: FeatureService,
  @OptIn(LimberModule.Orgs::class) private val orgService: OrgService,
  private val orgRoleService: OrgRoleService,
  private val tenantService: TenantService,
  @OptIn(LimberModule.Users::class) private val userService: UserService,
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator
) : JwtClaimsRequestService {
  private val json = Json()

  @LimberModule.Orgs
  @LimberModule.Users
  override fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel {
    val user = getAccountOrCreateUser(request)
    return requestJwtClaimsForUser(user)
  }

  @LimberModule.Users
  private fun getAccountOrCreateUser(request: JwtClaimsRequestModel): UserModel {
    val tenant = checkNotNull(tenantService.getByAuth0ClientId(request.auth0ClientId))

    val existingUser = userService.findOnlyOrNull { orgGuid(tenant.orgGuid); emailAddress(request.emailAddress) }
    if (existingUser != null) return existingUser

    return userService.create(
      model = UserModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        identityProvider = false,
        superuser = false,
        orgGuid = tenant.orgGuid,
        firstName = request.firstName,
        lastName = request.lastName,
        emailAddress = request.emailAddress,
        profilePhotoUrl = request.profilePhotoUrl
      )
    )
  }

  @LimberModule.Orgs
  private fun requestJwtClaimsForUser(user: UserModel): JwtClaimsModel {
    val org = checkNotNull(orgService.get(user.orgGuid))
    val features = featureService.getByOrgGuid(user.orgGuid)
    val permissions = getPermissions(org, user.guid)
    val jwtFeatures = features.associate { Pair(it.guid, JwtFeature(getPermissions(it))) }
    return convertJwtToModel(
      org = JwtOrg(org.guid, org.name, permissions, jwtFeatures),
      roles = JwtRole.values().filter { user.hasRole(it) }.toSet(),
      user = JwtUser(user.guid, user.firstName, user.lastName)
    )
  }

  private fun getPermissions(org: OrgModel, userGuid: UUID): OrgPermissions {
    val orgPermissions = orgRoleService.getByAccountGuid(org.guid, userGuid).map { it.permissions }.toMutableSet()
    if (userGuid == org.ownerAccountGuid) orgPermissions.add(ORG_OWNER_ORG_ROLE)
    return orgPermissions.union()
  }

  private fun getPermissions(feature: FeatureModel): FeaturePermissions = when (feature.type) {
    FeatureModel.Type.FORMS -> FormsFeaturePermissions.none()
    FeatureModel.Type.HOME -> HomeFeaturePermissions.none()
  }

  private fun convertJwtToModel(org: JwtOrg, roles: Set<JwtRole>, user: JwtUser) = JwtClaimsModel(
    org = json.stringify(org),
    roles = json.stringifySet(roles),
    user = json.stringify(user)
  )
}
