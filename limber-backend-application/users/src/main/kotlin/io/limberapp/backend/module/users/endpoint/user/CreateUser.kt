package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.users.mapper.user.UserMapper
import io.limberapp.backend.module.users.rep.user.UserRep
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand

internal class CreateUser @Inject constructor(
    application: Application,
    private val userService: UserService,
    private val userMapper: UserMapper
) : ApiEndpoint<CreateUser.Command, UserRep.Complete>(application, config) {

    internal data class Command(
        val creationRep: UserRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command): UserRep.Complete {
        val completeModel = userService.create(userMapper.creationModel(command.creationRep))
        return userMapper.completeRep(completeModel)
    }

    companion object {
        val config = Config(HttpMethod.Post, "/users")
    }
}
