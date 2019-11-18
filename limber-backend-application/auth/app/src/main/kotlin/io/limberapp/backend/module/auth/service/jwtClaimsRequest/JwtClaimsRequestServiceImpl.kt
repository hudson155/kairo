package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.users.model.user.UserModel
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtOrg
import io.limberapp.framework.endpoint.authorization.jwt.JwtUser
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.util.uuidGenerator.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime

internal class JwtClaimsRequestServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val userService: UserService,
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) : JwtClaimsRequestService {

    private val objectMapper = LimberObjectMapper()

    override fun requestJwtClaims(model: JwtClaimsRequestModel): JwtClaimsModel {
        val user = getOrCreateUser(model)
        val orgs = orgService.getByMemberId(user.id)
        val jwt = createJwt(user, orgs)
        return convertJwtToModel(jwt)
    }

    private fun getOrCreateUser(jwtClaimsRequest: JwtClaimsRequestModel): UserModel {
        val existingUserAccount = userService.getByEmailAddress(jwtClaimsRequest.emailAddress)
        if (existingUserAccount != null) return existingUserAccount
        val model = UserModel(
            id = uuidGenerator.generate(),
            created = LocalDateTime.now(clock),
            version = 0,
            firstName = jwtClaimsRequest.firstName,
            lastName = jwtClaimsRequest.lastName,
            emailAddress = jwtClaimsRequest.emailAddress,
            profilePhotoUrl = jwtClaimsRequest.profilePhotoUrl
        )
        userService.create(model)
        return model
    }

    private fun createJwt(
        user: UserModel,
        orgs: List<OrgModel>
    ) = Jwt(
        orgs = orgs.associate { Pair(it.id, JwtOrg(it.name)) },
        roles = emptySet(),
        user = JwtUser(user.id, user.firstName, user.lastName)
    )

    private fun convertJwtToModel(jwt: Jwt): JwtClaimsModel {
        return JwtClaimsModel(
            orgs = objectMapper.writeValueAsString(jwt.orgs),
            roles = objectMapper.writeValueAsString(jwt.roles),
            user = objectMapper.writeValueAsString(jwt.user)
        )
    }
}
