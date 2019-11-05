package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.users.service.user.UserService
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class DeleteUser @Inject constructor(
    application: Application,
    private val userService: UserService
) : ApiEndpoint<DeleteUser.Command, Unit>(application, config) {

    internal data class Command(
        val userId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        userId = call.parameters.getAsType(UUID::class, userId)
    )

    override fun authorization(command: Command) = Authorization.User(command.userId)

    override suspend fun handler(command: Command) {
        userService.delete(command.userId)
    }

    companion object {
        const val userId = "userId"
        val config = Config(HttpMethod.Delete, "/users/{$userId}")
    }
}
