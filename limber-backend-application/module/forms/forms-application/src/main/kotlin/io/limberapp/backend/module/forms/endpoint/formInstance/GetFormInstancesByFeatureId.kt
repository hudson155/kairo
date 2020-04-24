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
internal class GetFormInstancesByFeatureId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<FormInstanceApi.GetByFeatureId, List<FormInstanceRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.GetByFeatureId::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.GetByFeatureId(
        featureId = call.parameters.getAsType(UUID::class, "featureId")
    )

    override suspend fun Handler.handle(command: FormInstanceApi.GetByFeatureId): List<FormInstanceRep.Complete> {
        Authorization.HasAccessToFeature(command.featureId).authorize()
        val models = formInstanceService.getByFeatureId(command.featureId)
        return models.map { formInstanceMapper.completeRep(it) }
    }
}
