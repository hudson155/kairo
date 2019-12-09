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
 * Creates a new part within a form template.
 */
internal class CreateFormTemplatePart @Inject constructor(
    application: Application,
    servingConfig: ServingConfig
) : LimberApiEndpoint<CreateFormTemplatePart.Command, Nothing>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val index: Short,
        val creationRep: FormTemplatePartRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        index = call.parameters.getAsType(Short::class, index),
        creationRep = call.getAndValidateBody()
    )

    override fun authorization(command: Command) = TODO()

    override suspend fun handler(command: Command) = TODO()

    companion object {
        const val formTemplateId = "formTemplateId"
        const val index = "index"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts")
            )
        )
    }
}
