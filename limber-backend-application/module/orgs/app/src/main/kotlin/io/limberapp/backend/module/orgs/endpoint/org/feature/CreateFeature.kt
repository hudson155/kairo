package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.module.annotation.Service
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.api.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

/**
 * Creates a new feature within an org. This must be done before creating the feature's implementation in the
 * corresponding module.
 */
internal class CreateFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val featureService: FeatureService,
    private val featureMapper: FeatureMapper
) : LimberApiEndpoint<CreateFeature.Command, FeatureRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val creationRep: FeatureRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FeatureRep.Complete {
        Authorization.OrgMember(command.orgId).authorize()
        val model = featureMapper.model(command.creationRep)
        featureService.create(command.orgId, model)
        return featureMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("orgs"), VariableComponent(orgId), StringComponent("features"))
        )
    }
}
