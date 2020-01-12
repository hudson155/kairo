package io.limberapp.backend.module.forms.endpoint.formTemplate.part

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
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplatePartService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Deletes a existing part from a form template.
 */
internal class DeleteFormTemplatePart @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val formTemplateService: FormTemplateService,
    @Service private val formTemplatePartService: FormTemplatePartService
) : LimberApiEndpoint<DeleteFormTemplatePart.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val partId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        partId = call.parameters.getAsType(UUID::class, partId)
    )

    override suspend fun Handler.handle(command: Command) {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        formTemplatePartService.delete(command.formTemplateId, command.partId)
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        const val partId = "partId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts"),
                VariableComponent(partId)
            )
        )
    }
}
