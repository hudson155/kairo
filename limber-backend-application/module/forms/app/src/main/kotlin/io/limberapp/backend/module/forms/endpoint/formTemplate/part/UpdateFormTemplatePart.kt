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
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplatePartRep
import java.util.UUID

/**
 * Updates an form template part's information.
 */
internal class UpdateFormTemplatePart @Inject constructor(
    application: Application,
    servingConfig: ServingConfig
) : LimberApiEndpoint<UpdateFormTemplatePart.Command, FormTemplatePartRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val partId: UUID,
        val updateRep: FormTemplatePartRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        partId = call.parameters.getAsType(UUID::class, partId),
        updateRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command) = TODO()

    companion object {
        const val formTemplateId = "formTemplateId"
        const val partId = "partId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts"),
                VariableComponent(partId)
            )
        )
    }
}
