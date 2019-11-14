package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.users.mapper.api.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.framework.config.Config
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Updates a user's information.
 */
internal class UpdateUser @Inject constructor(
    application: Application,
    config: Config,
    private val userService: UserService,
    private val userMapper: UserMapper
) : ApiEndpoint<UpdateUser.Command, UserRep.Complete>(
    application,
    pathPrefix = config.serving.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID,
        val updateRep: UserRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId),
        updateRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.User(command.userId)

    override suspend fun handler(command: Command): UserRep.Complete {
        val completeModel = userService.update(
            id = command.userId,
            model = userMapper.updateModel(command.updateRep)
        )
        return userMapper.completeRep(completeModel)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(HttpMethod.Patch, "/users/{$userId}")
    }
}
