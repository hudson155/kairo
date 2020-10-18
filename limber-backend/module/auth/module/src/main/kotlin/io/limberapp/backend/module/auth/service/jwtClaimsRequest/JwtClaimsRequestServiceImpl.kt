package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.client.account.UserClient
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.auth.jwt.JwtFeature
import io.limberapp.common.auth.jwt.JwtOrg
import io.limberapp.common.auth.jwt.JwtUser
import io.limberapp.common.serialization.limberObjectMapper
import io.limberapp.permissions.AccountRole
import io.limberapp.permissions.featurePermissions.FeaturePermissions
import io.limberapp.permissions.featurePermissions.FeaturePermissions.Companion.unionIfSameType
import io.limberapp.permissions.orgPermissions.OrgPermission
import io.limberapp.permissions.orgPermissions.OrgPermissions
import io.limberapp.permissions.orgPermissions.OrgPermissions.Companion.union
import java.util.*

private val ORG_OWNER_ORG_ROLE =
    OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS))

internal class JwtClaimsRequestServiceImpl @Inject constructor(
    private val featureRoleService: FeatureRoleService,
    private val orgRoleService: OrgRoleService,
    private val tenantService: TenantService,
    private val orgClient: OrgClient,
    private val userClient: UserClient,
) : JwtClaimsRequestService {
  private val objectMapper = limberObjectMapper()

  override suspend fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel {
    val user = getAccountOrCreateUser(request)
    return requestJwtClaimsForUser(user)
  }

  private suspend fun getAccountOrCreateUser(request: JwtClaimsRequestModel): UserRep.Complete {
    val tenant = checkNotNull(tenantService.getByAuth0ClientId(request.auth0ClientId))

    val existingUser = userClient(UserApi.GetByOrgGuidAndEmailAddress(tenant.orgGuid, request.emailAddress))
    if (existingUser != null) return existingUser

    return userClient(UserApi.Post(
        rep = UserRep.Creation(
            orgGuid = tenant.orgGuid,
            firstName = request.firstName,
            lastName = request.lastName,
            emailAddress = request.emailAddress,
            profilePhotoUrl = request.profilePhotoUrl
        )
    ))
  }

  private suspend fun requestJwtClaimsForUser(user: UserRep.Complete): JwtClaimsModel {
    val org = checkNotNull(orgClient(OrgApi.Get(user.orgGuid)))
    val (orgRoles, isOwner, permissions) = getPermissions(org, user.guid)
    val orgRoleGuids = orgRoles.map { it.guid }.toSet()
    val jwtFeatures = org.features
        .mapNotNull { feature -> getPermissions(feature, orgRoleGuids)?.let { Pair(feature.guid, JwtFeature(it)) } }
        .associate { it }
    return convertJwtToModel(
        org = JwtOrg(org.guid, org.name, isOwner, permissions, jwtFeatures),
        roles = AccountRole.values().filter { it in user.roles }.toSet(),
        user = JwtUser(user.guid, user.firstName, user.lastName)
    )
  }

  private fun getPermissions(org: OrgRep.Complete, userGuid: UUID): Triple<Set<OrgRoleModel>, Boolean, OrgPermissions> {
    val orgRoles = orgRoleService.getByAccountGuid(orgGuid = org.guid, accountGuid = userGuid)
    val orgPermissions = orgRoles.map { it.permissions }.toMutableSet()
    val isOwner = userGuid == org.ownerUserGuid
    if (isOwner) orgPermissions.add(ORG_OWNER_ORG_ROLE)
    return Triple(orgRoles, isOwner, orgPermissions.union())
  }

  private fun getPermissions(feature: FeatureRep.Complete, orgRoleGuids: Set<UUID>): FeaturePermissions? {
    val featureRoles = featureRoleService.getByOrgRoleGuids(feature.guid, orgRoleGuids)
    val featurePermissions = featureRoles.map { it.permissions }
    return featurePermissions.unionIfSameType()
  }

  private fun convertJwtToModel(org: JwtOrg, roles: Set<AccountRole>, user: JwtUser) = JwtClaimsModel(
      org = objectMapper.writeValueAsString(org),
      roles = objectMapper.writeValueAsString(roles),
      user = objectMapper.writeValueAsString(user)
  )
}
