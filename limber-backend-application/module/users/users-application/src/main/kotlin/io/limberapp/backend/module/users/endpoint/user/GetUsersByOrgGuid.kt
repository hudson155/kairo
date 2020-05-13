package io.limberapp.backend.module.users.endpoint.user

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.backend.module.users.mapper.account.UserMapper
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.backend.module.users.service.account.UserService
import java.util.UUID

/**
 * Returns all users that are a member of the given org.
 */
internal class GetUsersByOrgGuid @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val userService: UserService,
    private val userMapper: UserMapper
) : LimberApiEndpoint<UserApi.GetByOrgGuid, Set<UserRep.Summary>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = UserApi.GetByOrgGuid::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = UserApi.GetByOrgGuid(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
    )

    override suspend fun Handler.handle(command: UserApi.GetByOrgGuid): Set<UserRep.Summary> {
        Authorization.OrgMember(command.orgGuid).authorize()
        val users = userService.getByOrgGuid(command.orgGuid)
        return users.map { userMapper.summaryRep(it) }.toSet()
    }
}
