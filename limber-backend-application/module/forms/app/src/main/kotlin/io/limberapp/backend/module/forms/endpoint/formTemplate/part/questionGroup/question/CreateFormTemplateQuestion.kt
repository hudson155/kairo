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
import io.limberapp.backend.module.forms.mapper.api.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Creates a new question within a form template's question group.
 */
internal class CreateFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : LimberApiEndpoint<CreateFormTemplateQuestion.Command, FormTemplateQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val partId: UUID,
        val questionGroupId: UUID,
        val index: Int?,
        val creationRep: FormTemplateQuestionRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        partId = call.parameters.getAsType(UUID::class, partId),
        questionGroupId = call.parameters.getAsType(UUID::class, questionGroupId),
        index = call.parameters.getAsType(Int::class, index, optional = true),
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FormTemplateQuestionRep.Complete {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val model = formTemplateQuestionMapper.model(command.creationRep)
        formTemplateQuestionService.create(
            formTemplateId = command.formTemplateId,
            formTemplatePartId = command.partId,
            formTemplateQuestionGroupId = command.questionGroupId,
            model = model,
            index = command.index
        )
        return formTemplateQuestionMapper.completeRep(model)
    }

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
