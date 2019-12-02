package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.api.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Updates a feature's information.
 */
internal class UpdateFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService,
    private val featureMapper: FeatureMapper
) : LimberApiEndpoint<UpdateFeature.Command, FeatureRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val featureId: UUID,
        val updateRep: FeatureRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        featureId = call.parameters.getAsType(UUID::class, featureId),
        updateRep = call.getAndValidateBody()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): FeatureRep.Complete {
        val update = featureMapper.update(command.updateRep)
        val model = orgService.updateFeature(command.orgId, command.featureId, update)
        return featureMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        const val featureId = "featureId"
        val endpointConfig = EndpointConfig(HttpMethod.Patch, "/orgs/{$orgId}/features/{$featureId}")
    }
}
