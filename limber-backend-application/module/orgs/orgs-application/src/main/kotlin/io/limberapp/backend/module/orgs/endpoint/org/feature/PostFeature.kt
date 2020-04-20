package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.mapper.org.FeatureMapper
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

/**
 * Creates a new feature within an org. This must be done before creating the feature's implementation in the
 * corresponding module.
 */
internal class PostFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper
) : LimberApiEndpoint<OrgFeatureApi.Post, FeatureRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgFeatureApi.Post::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = OrgFeatureApi.Post(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: OrgFeatureApi.Post): FeatureRep.Complete {
        Authorization.OrgMember(command.orgId).authorize()
        val model = featureMapper.model(command.rep.required())
        featureService.create(command.orgId, model)
        return featureMapper.completeRep(model)
    }
}
