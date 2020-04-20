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
import io.limberapp.backend.module.forms.authorization.HasAccessToFormInstance
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Creates a new question (answer) within a form instance.
 */
internal class PutFormInstanceQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
    private val formInstanceQuestionMapper: FormInstanceQuestionMapper
) : LimberApiEndpoint<PutFormInstanceQuestion.Command, FormInstanceQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formInstanceId: UUID,
        val questionId: UUID,
        val creationRep: FormInstanceQuestionRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formInstanceId = call.parameters.getAsType(UUID::class, formInstanceId),
        questionId = call.parameters.getAsType(UUID::class, questionId),
        creationRep = call.getAndValidateBody<FormInstanceQuestionRep.Creation>().required()
    )

    override suspend fun Handler.handle(command: Command): FormInstanceQuestionRep.Complete {
        HasAccessToFormInstance(formInstanceService, command.formInstanceId).authorize()
        val model = formInstanceQuestionMapper.model(command.questionId, command.creationRep)
        formInstanceQuestionService.upsert(
            formInstanceId = command.formInstanceId,
            model = model
        )
        return formInstanceQuestionMapper.completeRep(model)
    }

    companion object {
        const val formInstanceId = "formInstanceId"
        const val questionId = "questionId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Put,
            pathTemplate = listOf(
                StringComponent("form-instances"),
                VariableComponent(formInstanceId),
                StringComponent("questions"),
                VariableComponent(questionId)
            )
        )
    }
}
