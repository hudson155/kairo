package io.limberapp.backend.module.orgs.endpoint.org.role

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.role.OrgRoleApi
import io.limberapp.backend.module.orgs.service.org.OrgRoleService
import java.util.UUID

/**
 * Deletes a role from an org.
 */
internal class DeleteOrgRole @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgRoleService: OrgRoleService
) : LimberApiEndpoint<OrgRoleApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgRoleApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.Delete(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
        orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid")
    )

    override suspend fun Handler.handle(command: OrgRoleApi.Delete) {
        Authorization.OrgMember(command.orgGuid).authorize()
        orgRoleService.delete(command.orgGuid, command.orgRoleGuid)
    }
}
