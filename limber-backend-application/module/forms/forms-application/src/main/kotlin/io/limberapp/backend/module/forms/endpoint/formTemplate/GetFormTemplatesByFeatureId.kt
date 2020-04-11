package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
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
) : LimberApiEndpoint<GetFormTemplatesByFeatureId.Command, List<FormTemplateRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val featureId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        featureId = call.parameters.getAsType(UUID::class, featureId)
    )

    override suspend fun Handler.handle(command: Command): List<FormTemplateRep.Complete> {
        Authorization.HasAccessToFeature(command.featureId).authorize()
        val models = formTemplateService.getByFeatureId(command.featureId)
        return models.map { formTemplateMapper.completeRep(it) }
    }

    companion object {
        const val featureId = "featureId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("form-templates"))
        )
    }
}
