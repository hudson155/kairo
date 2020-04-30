package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.exception.account.UserNotFound
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import java.util.UUID

/**
 * Returns a single user.
 */
internal class GetUser @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<UserApi.Get, UserRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = UserApi.Get::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = UserApi.Get(
        userGuid = call.parameters.getAsType(UUID::class, "userGuid")
    )

    override suspend fun Handler.handle(command: UserApi.Get): UserRep.Complete {
        Authorization.User(command.userGuid).authorize()
        val user = userService.get(command.userGuid) ?: throw UserNotFound()
        return userMapper.completeRep(user)
    }
}
