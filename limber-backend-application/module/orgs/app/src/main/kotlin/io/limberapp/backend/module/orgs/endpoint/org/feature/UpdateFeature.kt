package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.org.FeatureMapper
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

/**
 * Updates a feature's information.
 */
internal class UpdateFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val featureService: FeatureService,
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

    override suspend fun Handler.handle(command: Command): FeatureRep.Complete {
        Authorization.OrgMember(command.orgId).authorize()
        val update = featureMapper.update(command.updateRep)
        val model = featureService.update(command.orgId, command.featureId, update)
        return featureMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        const val featureId = "featureId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(
                StringComponent("orgs"),
                VariableComponent(orgId),
                StringComponent("features"),
                VariableComponent(featureId)
            )
        )
    }
}
