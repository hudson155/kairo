package io.limberapp.backend.module.forms.endpoint.formTemplate.part

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
import io.limberapp.backend.module.forms.mapper.api.formTemplate.FormTemplatePartMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplatePartRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplatePartService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Creates a new part within a form template.
 */
internal class CreateFormTemplatePart @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplatePartService: FormTemplatePartService,
    private val formTemplatePartMapper: FormTemplatePartMapper
) : LimberApiEndpoint<CreateFormTemplatePart.Command, FormTemplatePartRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val index: Int?,
        val creationRep: FormTemplatePartRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        index = call.parameters.getAsType(Int::class, index, optional = true),
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FormTemplatePartRep.Complete {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val model = formTemplatePartMapper.model(command.creationRep)
        formTemplatePartService.create(command.formTemplateId, model, command.index)
        return formTemplatePartMapper.completeRep(model)
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        const val index = "index"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts")
            )
        )
    }
}
