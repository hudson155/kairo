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
 * Returns a single user.
 */
internal class GetUser @Inject constructor(
    application: Application,
    servingConfig: com.piperframework.config.serving.ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : com.piperframework.endpoint.ApiEndpoint<GetUser.Command, UserRep.Complete?>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID
    ) : com.piperframework.endpoint.command.AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId)
    )

    override fun authorization(command: Command) = Authorization.User(command.userId)

    override suspend fun handler(command: Command): UserRep.Complete? {
        val model = userService.get(command.userId) ?: return null
        return userMapper.completeRep(model)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = com.piperframework.endpoint.EndpointConfig(HttpMethod.Get, "/users/{$userId}")
    }
}
