package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import com.piperframework.serialization.Json
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.authorization.permissions.OrgPermissions.Companion.union
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.service.org.OrgRoleMembershipService
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import java.time.Clock
import java.time.LocalDateTime

internal class JwtClaimsRequestServiceImpl @Inject constructor(
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgRoleService: OrgRoleService,
    private val tenantService: TenantService,
    private val userService: UserService,
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) : JwtClaimsRequestService {
    private val json = Json()

    override fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel {
        val user = getAccountOrCreateUser(request)
        return requestJwtClaimsForUser(user)
    }

    private fun getAccountOrCreateUser(request: JwtClaimsRequestModel): UserModel {
        val existingUser = userService.getByEmailAddress(request.emailAddress)
        if (existingUser != null) return existingUser

        val tenant = checkNotNull(tenantService.getByAuth0ClientId(request.auth0ClientId))

        val newUser = UserModel(
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
        userService.create(newUser)

        return newUser
    }

    private fun requestJwtClaimsForUser(user: UserModel): JwtClaimsModel {
        val org = checkNotNull(orgService.get(user.orgGuid))
        val features = featureService.getByOrgGuid(user.orgGuid)
        val permissions = orgRoleService.getByAccountGuid(org.guid, user.guid).map { it.permissions }.union()
        return convertJwtToModel(
            org = JwtOrg(org.guid, org.name, permissions, features.map { it.guid }),
            roles = JwtRole.values().filter { user.hasRole(it) }.toSet(),
            user = JwtUser(user.guid, user.firstName, user.lastName)
        )
    }

    private fun convertJwtToModel(org: JwtOrg, roles: Set<JwtRole>, user: JwtUser): JwtClaimsModel = JwtClaimsModel(
        org = json.stringify(org),
        roles = json.stringifySet(roles),
        user = json.stringify(user)
    )
}
