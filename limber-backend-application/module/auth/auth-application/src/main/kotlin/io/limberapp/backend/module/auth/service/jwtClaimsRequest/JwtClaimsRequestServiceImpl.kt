package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import com.piperframework.serialization.Json
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.account.AccountModel
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.AccountService
import io.limberapp.backend.module.users.service.account.UserService
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

internal class JwtClaimsRequestServiceImpl @Inject constructor(
    private val accountService: AccountService,
    private val orgService: OrgService,
    private val tenantService: TenantService,
    private val userService: UserService,
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) : JwtClaimsRequestService {
    private val json = Json()

    override fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel {
        val user = getAccountOrCreateUser(request)
        val account = checkNotNull(accountService.get(user.id))
        return requestJwtClaimsForUser(account, user)
    }

    override fun requestJwtClaimsForExistingUser(accountId: UUID): JwtClaimsModel? {
        val account = accountService.get(accountId) ?: return null
        val user = userService.get(accountId)
        return requestJwtClaimsForUser(account, user)
    }

    private fun requestJwtClaimsForUser(account: AccountModel, user: UserModel?): JwtClaimsModel {
        val org = user?.let { checkNotNull(orgService.get(it.orgId)) }
        val jwt = createJwt(account, user, org)
        return convertJwtToModel(jwt)
    }

    private fun getAccountOrCreateUser(request: JwtClaimsRequestModel): UserModel {
        val existingUser = userService.getByEmailAddress(request.emailAddress)
        if (existingUser != null) return existingUser

        val tenant = checkNotNull(tenantService.getByAuth0ClientId(request.auth0ClientId))

        val newUser = UserModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            orgId = tenant.orgId,
            firstName = request.firstName,
            lastName = request.lastName,
            emailAddress = request.emailAddress,
            profilePhotoUrl = request.profilePhotoUrl,
            roles = emptySet()
        )
        userService.create(newUser)

        return newUser
    }

    private fun createJwt(account: AccountModel, user: UserModel?, org: OrgModel?): Jwt {
        return Jwt(
            org = org?.let { JwtOrg(it.id, it.name, it.features.map { it.id }) },
            roles = account.roles.toList(),
            user = JwtUser(account.id, user?.firstName, user?.lastName)
        )
    }

    private fun convertJwtToModel(jwt: Jwt): JwtClaimsModel = JwtClaimsModel(
        org = jwt.org?.let { json.stringify(it) },
        roles = json.stringifyList(jwt.roles),
        user = json.stringify(jwt.user)
    )
}
