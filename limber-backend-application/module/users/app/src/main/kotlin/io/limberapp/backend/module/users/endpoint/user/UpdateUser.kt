package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.module.users.mapper.api.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Updates a user's information.
 */
internal class UpdateUser @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : ApiEndpoint<UpdateUser.Command, UserRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID,
        val updateRep: UserRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId),
        updateRep = call.getAndValidateBody()
    )

    override fun authorization(command: Command) = Authorization.User(command.userId)

    override suspend fun handler(command: Command): UserRep.Complete {
        val update = userMapper.update(command.updateRep)
        val model = userService.update(command.userId, update)
        return userMapper.completeRep(model)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(HttpMethod.Patch, "/users/{$userId}")
    }
}
