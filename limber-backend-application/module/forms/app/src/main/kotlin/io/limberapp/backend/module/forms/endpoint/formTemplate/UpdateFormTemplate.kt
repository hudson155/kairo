package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormTemplate
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Updates an form template's information.
 */
internal class UpdateFormTemplate @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateMapper: FormTemplateMapper
) : LimberApiEndpoint<UpdateFormTemplate.Command, FormTemplateRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val updateRep: FormTemplateRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        updateRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FormTemplateRep.Complete {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val update = formTemplateMapper.update(command.updateRep)
        val model = formTemplateService.update(command.formTemplateId, update)
        return formTemplateMapper.completeRep(model)
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(StringComponent("form-templates"), VariableComponent(formTemplateId))
        )
    }
}
