package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.util.uuid.singleNullOrThrow
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
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
    private val userService: UserService,
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) : JwtClaimsRequestService {

    private val objectMapper = PiperObjectMapper()

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
        val orgs = orgService.getByMemberId(account.id)
        val jwt = createJwt(account, user, orgs)
        return convertJwtToModel(jwt)
    }

    private fun getAccountOrCreateUser(request: JwtClaimsRequestModel): UserModel {
        val existingUser = userService.getByEmailAddress(request.emailAddress)
        if (existingUser != null) return existingUser
        val newUser = UserModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            firstName = request.firstName,
            lastName = request.lastName,
            emailAddress = request.emailAddress,
            profilePhotoUrl = request.profilePhotoUrl,
            roles = emptySet()
        )
        userService.create(newUser)
        return newUser
    }

    private fun createJwt(account: AccountModel, user: UserModel?, orgs: Set<OrgModel>): Jwt {
        check(orgs.size <= 1)
        return Jwt(
            org = orgs.singleNullOrThrow()?.let { JwtOrg(it.id, it.name) },
            roles = account.roles,
            user = JwtUser(account.id, user?.firstName, user?.lastName)
        )
    }

    private fun convertJwtToModel(jwt: Jwt): JwtClaimsModel = JwtClaimsModel(
        org = objectMapper.writeValueAsString(jwt.org),
        roles = objectMapper.writeValueAsString(jwt.roles),
        user = objectMapper.writeValueAsString(jwt.user)
    )
}
