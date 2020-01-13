package io.limberapp.backend.module.forms.endpoint.formTemplate.question

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
 * Updates a form template question's information.
 */
internal class UpdateFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig
) : LimberApiEndpoint<UpdateFormTemplateQuestion.Command, FormTemplateQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val questionId: UUID,
        val updateRep: FormTemplateQuestionRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        questionId = call.parameters.getAsType(UUID::class, questionId),
        updateRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command) = TODO()

    companion object {
        const val formTemplateId = "formTemplateId"
        const val questionId = "questionId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("questions"),
                VariableComponent(questionId)
            )
        )
    }
}
