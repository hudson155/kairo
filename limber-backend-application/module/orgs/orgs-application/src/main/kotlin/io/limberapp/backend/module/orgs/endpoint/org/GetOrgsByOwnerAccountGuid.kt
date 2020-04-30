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
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Returns all orgs with the given owner.
 */
internal class GetOrgsByOwnerAccountGuid @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<OrgApi.GetByOwnerAccountGuid, Set<OrgRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgApi.GetByOwnerAccountGuid::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgApi.GetByOwnerAccountGuid(
        ownerAccountGuid = call.parameters.getAsType(UUID::class, "ownerAccountGuid")
    )

    override suspend fun Handler.handle(command: OrgApi.GetByOwnerAccountGuid): Set<OrgRep.Complete> {
        Authorization.User(command.ownerAccountGuid).authorize()
        val orgs = orgService.getByOwnerAccountGuid(command.ownerAccountGuid)
        check(orgs.size <= 1) // Accounts can't be part of multiple orgs.
        val org = orgs.singleOrNull() ?: return emptySet()
        val features = featureService.getByOrgGuid(org.guid)
        return setOf(orgMapper.completeRep(org, features))
    }
}
