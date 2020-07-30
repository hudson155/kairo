package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.LimberModule
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.service.account.UserService
import java.util.*

@OptIn(LimberModule.Orgs::class)
internal class DeleteUser @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val userService: UserService
) : LimberApiEndpoint<UserApi.Delete, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = UserApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = UserApi.Delete(
    userGuid = call.parameters.getAsType(UUID::class, "userGuid")
  )

  override suspend fun Handler.handle(command: UserApi.Delete) {
    Authorization.User(command.userGuid).authorize()
    userService.delete(command.userGuid)
  }
}
