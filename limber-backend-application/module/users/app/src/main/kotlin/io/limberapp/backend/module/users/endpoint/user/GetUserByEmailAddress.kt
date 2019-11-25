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

/**
 * Returns a single user with the given email address.
 */
internal class GetUserByEmailAddress @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : ApiEndpoint<GetUserByEmailAddress.Command, UserRep.Complete?>(
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

    override fun authorization(command: Command) = Authorization.AnyJwt

    override suspend fun handler(command: Command): UserRep.Complete? {
        val model = userService.getByEmailAddress(command.emailAddress) ?: return null
        return userMapper.completeRep(model)
    }

    override fun secondaryAuthorization(response: UserRep.Complete?) = Authorization.User(response?.id)

    companion object {
        const val emailAddress = "emailAddress"
        val endpointConfig = EndpointConfig(HttpMethod.Get, "/users")
    }
}
