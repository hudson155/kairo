package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.UUID

/**
 * Deletes a feature from an org. This does not delete the feature's implementation in the corresponding module. The
 * implementation is not deleted, in case it needs to be recovered.
 */
internal class DeleteFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val featureService: FeatureService
) : LimberApiEndpoint<OrgFeatureApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgFeatureApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgFeatureApi.Delete(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        featureId = call.parameters.getAsType(UUID::class, "featureId")
    )

    override suspend fun Handler.handle(command: OrgFeatureApi.Delete) {
        Authorization.OrgMember(command.orgId).authorize()
        featureService.delete(
            orgId = command.orgId,
            featureId = command.featureId
        )
    }
}
