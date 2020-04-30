package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Returns all form instances within the feature.
 */
internal class GetFormInstancesByFeatureGuid @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<FormInstanceApi.GetByFeatureGuid, Set<FormInstanceRep.Summary>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.GetByFeatureGuid::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.GetByFeatureGuid(
        featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
    )

    override suspend fun Handler.handle(command: FormInstanceApi.GetByFeatureGuid): Set<FormInstanceRep.Summary> {
        Authorization.HasAccessToFeature(command.featureGuid).authorize()
        val formInstances = formInstanceService.getByFeatureGuid(command.featureGuid)
        return formInstances.map { formInstanceMapper.summaryRep(it) }.toSet()
    }
}
