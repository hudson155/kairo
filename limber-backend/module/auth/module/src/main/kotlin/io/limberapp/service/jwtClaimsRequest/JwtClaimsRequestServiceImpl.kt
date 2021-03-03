package io.limberapp.service.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.api.org.OrgApi
import io.limberapp.api.user.UserApi
import io.limberapp.auth.jwt.Jwt
import io.limberapp.auth.jwt.JwtFeature
import io.limberapp.auth.jwt.JwtOrg
import io.limberapp.auth.jwt.JwtUser
import io.limberapp.client.org.OrgClient
import io.limberapp.client.user.UserClient
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.exception.tenant.TenantNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.permissions.feature.FeaturePermissions
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.permissions.org.OrgPermissions
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.rep.org.OrgRep
import io.limberapp.rep.user.UserRep
import io.limberapp.service.feature.FeatureRoleService
import io.limberapp.service.org.OrgRoleService
import io.limberapp.service.tenant.TenantService
import java.util.UUID

/**
 * These permissions are given automatically to the org owner.
 */
private val ORG_OWNER_ORG_ROLE =
    OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS))

internal class JwtClaimsRequestServiceImpl @Inject constructor(
    private val orgClient: OrgClient,
    private val userClient: UserClient,
    private val featureRoleService: FeatureRoleService,
    private val orgRoleService: OrgRoleService,
    private val tenantService: TenantService,
) : JwtClaimsRequestService {
  override suspend fun requestJwtClaims(request: JwtClaimsRequestModel): Jwt {
    val tenant = tenantService.getByAuth0ClientId(request.auth0ClientId)
        ?: throw TenantNotFound().unprocessable()
    val org = orgClient(OrgApi.Get(tenant.orgGuid))
        ?: throw OrgNotFound().unprocessable()
    val user = getOrCreateUser(org.guid, request)
    return requestJwtClaims(org, user)
  }

  private suspend fun getOrCreateUser(
      orgGuid: UUID,
      request: JwtClaimsRequestModel,
  ): UserRep.Complete {
    val existingUser = userClient(UserApi.GetByOrgGuidAndEmailAddress(
        orgGuid = orgGuid,
        emailAddress = request.emailAddress,
    ))
    if (existingUser != null) return existingUser

    return userClient(UserApi.Post(
        rep = UserRep.Creation(
            orgGuid = orgGuid,
            firstName = request.firstName,
            lastName = request.lastName,
            emailAddress = request.emailAddress,
            profilePhotoUrl = request.profilePhotoUrl,
        ),
    ))
  }

  private fun requestJwtClaims(org: OrgRep.Complete, user: UserRep.Complete): Jwt {
    val orgRoles = orgRoleService.getByUserGuid(orgGuid = org.guid, userGuid = user.guid)
    val orgPermissions = orgRoles.map { it.permissions }.toMutableSet()
    val isOwner = user.guid == org.ownerUserGuid
    if (isOwner) orgPermissions += ORG_OWNER_ORG_ROLE

    val orgRoleGuids = orgRoles.map { it.guid }.toSet()
    val jwtFeatures = org.features
        .mapNotNull { feature ->
          getPermissions(feature, orgRoleGuids)
              ?.let { Pair(feature.guid, JwtFeature(it)) }
        }
        .associate { it }

    return Jwt(
        permissions = user.permissions,
        org = JwtOrg(org.guid, org.name, isOwner, orgPermissions.union()),
        features = jwtFeatures,
        user = JwtUser(user.guid, user.firstName, user.lastName),
    )
  }

  private fun getPermissions(
      feature: FeatureRep.Complete,
      orgRoleGuids: Set<UUID>,
  ): FeaturePermissions? {
    val featureRoles = featureRoleService.getByOrgRoleGuids(feature.guid, orgRoleGuids)
    return featureRoles.map { it.permissions }.union()
  }

  private fun Collection<OrgPermissions>.union(): OrgPermissions =
      fold(OrgPermissions.none()) { acc, permissions -> acc + permissions }

  private fun Collection<FeaturePermissions>.union(): FeaturePermissions? =
      reduceOrNull { acc, permissions -> acc + permissions }
}
