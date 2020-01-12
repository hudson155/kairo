package io.limberapp.backend.module.forms.endpoint.formTemplate

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
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.mapper.api.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Returns a single form template.
 */
internal class GetFormTemplate @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val formTemplateService: FormTemplateService,
    private val formTemplateMapper: FormTemplateMapper
) : LimberApiEndpoint<GetFormTemplate.Command, FormTemplateRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId)
    )

    override suspend fun Handler.handle(command: Command): FormTemplateRep.Complete {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val model = formTemplateService.get(command.formTemplateId) ?: throw FormTemplateNotFound()
        return formTemplateMapper.completeRep(model)
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("form-templates"), VariableComponent(formTemplateId))
        )
    }
}
