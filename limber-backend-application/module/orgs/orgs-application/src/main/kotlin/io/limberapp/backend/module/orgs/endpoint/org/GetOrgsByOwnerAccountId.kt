package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Returns all orgs with the given owner.
 */
internal class GetOrgsByOwnerAccountId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<OrgApi.GetByOwnerAccountId, List<OrgRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgApi.GetByOwnerAccountId::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = OrgApi.GetByOwnerAccountId(
        ownerAccountId = call.parameters.getAsType(UUID::class, "ownerAccountId")
    )

    override suspend fun Handler.handle(command: OrgApi.GetByOwnerAccountId): List<OrgRep.Complete> {
        Authorization.User(command.ownerAccountId).authorize()
        val models = orgService.getByOwnerAccountId(command.ownerAccountId)
        return models.map { orgMapper.completeRep(it) }
    }
}
