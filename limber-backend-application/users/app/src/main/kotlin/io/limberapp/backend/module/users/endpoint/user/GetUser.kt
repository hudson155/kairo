package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
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
 * Returns a single user.
 */
internal class GetUser @Inject constructor(
    application: Application,
    config: Config,
    private val userService: UserService,
    private val userMapper: UserMapper
) : ApiEndpoint<GetUser.Command, UserRep.Complete?>(
    application = application,
    pathPrefix = config.serving.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId)
    )

    override fun authorization(command: Command) = Authorization.User(command.userId)

    override suspend fun handler(command: Command): UserRep.Complete? {
        val completeModel = userService.get(command.userId)
        return completeModel?.let { userMapper.completeRep(it) }
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(HttpMethod.Get, "/users/{$userId}")
    }
}
