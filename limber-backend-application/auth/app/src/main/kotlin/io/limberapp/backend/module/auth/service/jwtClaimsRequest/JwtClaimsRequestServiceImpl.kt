package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import com.google.inject.Inject
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
    private val uuidGenerator: UuidGenerator,
    private val clock: Clock
) : JwtClaimsRequestService {

    private val objectMapper = LimberObjectMapper()

    override fun requestJwtClaims(model: JwtClaimsRequestModel.Creation): JwtClaimsRequestModel.Complete {
        val user = getOrCreateUser(model)
        val orgs = orgService.getByMemberId(user.id)
        val jwt = createJwt(user, orgs)
        return convertJwtToModel(jwt)
    }

    private fun getOrCreateUser(model: JwtClaimsRequestModel.Creation): UserModel.Complete {
        val existingUserAccount = userService.getByEmailAddress(model.emailAddress)
        if (existingUserAccount != null) return existingUserAccount
        return userService.create(
            UserModel.Creation(
                id = uuidGenerator.generate(),
                created = LocalDateTime.now(clock),
                version = 0,
                firstName = model.firstName,
                lastName = model.lastName,
                emailAddress = model.emailAddress,
                profilePhotoUrl = model.profilePhotoUrl
            )
        )
    }

    private fun createJwt(
        user: UserModel.Complete,
        orgs: List<OrgModel.Complete>
    ) = Jwt(
        orgs = orgs.associate { Pair(it.id, JwtOrg(it.name)) },
        roles = emptySet(),
        user = JwtUser(user.id, user.firstName, user.lastName)
    )

    private fun convertJwtToModel(jwt: Jwt): JwtClaimsRequestModel.Complete {
        return JwtClaimsRequestModel.Complete(
            orgs = objectMapper.writeValueAsString(jwt.orgs),
            roles = objectMapper.writeValueAsString(jwt.roles),
            user = objectMapper.writeValueAsString(jwt.user)
        )
    }
}
