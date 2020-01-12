package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.module.annotation.Service
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.exception.notFound.UserNotFound
import io.limberapp.backend.module.users.mapper.api.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import java.util.UUID

/**
 * Returns a single user.
 */
internal class GetUser @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<GetUser.Command, UserRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val userId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId)
    )

    override suspend fun Handler.handle(command: Command): UserRep.Complete {
        Authorization.User(command.userId).authorize()
        val model = userService.get(command.userId) ?: throw UserNotFound()
        return userMapper.completeRep(model)
    }

    companion object {
        const val userId = "userId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("users"), VariableComponent(userId))
        )
    }
}
