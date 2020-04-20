package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import java.util.UUID

/**
 * Updates a user's information.
 */
internal class PatchUser @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<UserApi.Patch, UserRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = UserApi.Patch::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = UserApi.Patch(
        userId = call.parameters.getAsType(UUID::class, "userId"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: UserApi.Patch): UserRep.Complete {
        Authorization.User(command.userId).authorize()
        val update = userMapper.update(command.rep.required())
        val model = userService.update(command.userId, update)
        return userMapper.completeRep(model)
    }
}
