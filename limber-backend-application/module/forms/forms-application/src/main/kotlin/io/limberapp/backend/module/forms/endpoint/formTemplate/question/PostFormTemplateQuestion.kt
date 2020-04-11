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
import io.limberapp.backend.module.forms.authorization.HasAccessToFormTemplate
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Creates a new question within a form template.
 */
internal class PostFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : LimberApiEndpoint<PostFormTemplateQuestion.Command, FormTemplateQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formTemplateId: UUID,
        val rank: Int?,
        val creationRep: FormTemplateQuestionRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formTemplateId = call.parameters.getAsType(UUID::class, formTemplateId),
        rank = call.parameters.getAsType(Int::class, rank, optional = true),
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FormTemplateQuestionRep.Complete {
        HasAccessToFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val model = formTemplateQuestionMapper.model(command.creationRep)
        formTemplateQuestionService.create(
            formTemplateId = command.formTemplateId,
            model = model,
            rank = command.rank
        )
        return formTemplateQuestionMapper.completeRep(model)
    }

    companion object {
        const val formTemplateId = "formTemplateId"
        const val rank = "rank"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(
                StringComponent("form-templates"),
                VariableComponent(formTemplateId),
                StringComponent("questions")
            )
        )
    }
}
