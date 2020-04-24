package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Deletes an existing org.
 */
internal class DeleteOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService
) : LimberApiEndpoint<OrgApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Delete(
        orgId = call.parameters.getAsType(UUID::class, "orgId")
    )

    override suspend fun Handler.handle(command: OrgApi.Delete) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        orgService.delete(command.orgId)
    }
}
