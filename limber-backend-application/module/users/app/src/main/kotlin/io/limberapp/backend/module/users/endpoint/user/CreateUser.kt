package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService

/**
 * Creates a new user.
 */
internal class CreateUser @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<CreateUser.Command, UserRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val creationRep: UserRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): UserRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val model = userMapper.model(command.creationRep)
        userService.create(model)
        return userMapper.completeRep(model)
    }

    companion object {
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("users"))
        )
    }
}
