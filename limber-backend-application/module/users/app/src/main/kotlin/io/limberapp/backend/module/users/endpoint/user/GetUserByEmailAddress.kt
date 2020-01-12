package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
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

/**
 * Returns a single user with the given email address.
 */
internal class GetUserByEmailAddress @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<GetUserByEmailAddress.Command, UserRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val emailAddress: String
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        emailAddress = call.parameters.getAsType(String::class, emailAddress)
    )

    override suspend fun Handler.handle(command: Command): UserRep.Complete {
        Authorization.AnyJwt.authorize()
        val model = userService.getByEmailAddress(command.emailAddress) ?: throw UserNotFound()
        Authorization.User(model.id).authorize()
        return userMapper.completeRep(model)
    }

    companion object {
        const val emailAddress = "emailAddress"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("users"))
        )
    }
}
