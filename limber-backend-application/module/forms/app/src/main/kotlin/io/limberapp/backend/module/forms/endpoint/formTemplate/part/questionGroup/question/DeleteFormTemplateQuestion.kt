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
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormTemplate
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Deletes a existing question from a form template's question group.
 */
internal class DeleteFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : LimberApiEndpoint<DeleteFormTemplateQuestion.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val partId: UUID,
        val questionGroupId: UUID,
        val questionId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        partId = call.parameters.getAsType(UUID::class, partId),
        questionGroupId = call.parameters.getAsType(UUID::class, questionGroupId),
        questionId = call.parameters.getAsType(UUID::class, questionId)
    )

    override suspend fun Handler.handle(command: Command) {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        formTemplateQuestionService.delete(
            formTemplateId = command.formTemplateId,
            formTemplatePartId = command.partId,
            formTemplateQuestionGroupId = command.questionGroupId,
            formTemplateQuestionId = command.questionId
        )
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        const val partId = "partId"
        const val questionGroupId = "questionGroupId"
        const val questionId = "questionId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("parts"),
                VariableComponent(partId),
                StringComponent("question-groups"),
                VariableComponent(questionGroupId),
                StringComponent("questions"),
                VariableComponent(questionId)
            )
        )
    }
}
