package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Returns all form templates within the feature.
 */
internal class GetFormTemplatesByFeatureId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateMapper: FormTemplateMapper
) : LimberApiEndpoint<FormTemplateApi.GetByFeatureId, List<FormTemplateRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormTemplateApi.GetByFeatureId::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.GetByFeatureId(
        featureId = call.parameters.getAsType(UUID::class, "featureId")
    )

    override suspend fun Handler.handle(command: FormTemplateApi.GetByFeatureId): List<FormTemplateRep.Complete> {
        Authorization.HasAccessToFeature(command.featureId).authorize()
        val models = formTemplateService.getByFeatureId(command.featureId)
        return models.map { formTemplateMapper.completeRep(it) }
    }
}
