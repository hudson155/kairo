package io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question

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
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import java.util.UUID

/**
 * Creates a new question within a form template's question group.
 */
internal class CreateFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig
) : LimberApiEndpoint<CreateFormTemplateQuestion.Command, FormTemplateQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val partId: UUID,
        val questionGroupId: UUID,
        val index: Short?,
        val creationRep: FormTemplateQuestionRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        partId = call.parameters.getAsType(UUID::class, partId),
        questionGroupId = call.parameters.getAsType(UUID::class, questionGroupId),
        index = call.parameters.getAsType(Short::class, index, optional = true),
        creationRep = call.getAndValidateBody()
    )

    override fun authorization(command: Command) = TODO()

    override suspend fun handler(command: Command) = TODO()

    companion object {
        const val formTemplateId = "formTemplateId"
        const val partId = "partId"
        const val questionGroupId = "questionGroupId"
        const val index = "index"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts"),
                VariableComponent(partId),
                StringComponent("question-groups"),
                VariableComponent(questionGroupId),
                StringComponent("questions")
            )
        )
    }
}
