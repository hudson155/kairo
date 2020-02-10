package io.limberapp.backend.module.forms.endpoint.formInstance.question

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
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormInstance
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Deletes a existing question (answer) from a form instance.
 */
internal class DeleteFormInstanceQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService
) : LimberApiEndpoint<DeleteFormInstanceQuestion.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formInstanceId: UUID,
        val questionId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formInstanceId = call.parameters.getAsType(UUID::class, formInstanceId),
        questionId = call.parameters.getAsType(UUID::class, questionId)
    )

    override suspend fun Handler.handle(command: Command) {
        MemberOfOrgThatOwnsFormInstance(formInstanceService, command.formInstanceId).authorize()
        formInstanceQuestionService.delete(
            formInstanceId = command.formInstanceId,
            formTemplateQuestionId = command.questionId
        )
    }

    companion object {
        const val formInstanceId = "formInstanceId"
        const val questionId = "questionId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("form-instances"),
                VariableComponent(formInstanceId),
                StringComponent("questions"),
                VariableComponent(questionId)
            )
        )
    }
}
