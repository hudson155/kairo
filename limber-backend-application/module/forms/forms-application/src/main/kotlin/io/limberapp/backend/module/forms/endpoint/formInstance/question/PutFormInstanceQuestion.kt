package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
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
) : LimberApiEndpoint<FormInstanceQuestionApi.Put, FormInstanceQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceQuestionApi.Put::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceQuestionApi.Put(
        formInstanceId = call.parameters.getAsType(UUID::class, "formInstanceId"),
        questionId = call.parameters.getAsType(UUID::class, "questionId"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: FormInstanceQuestionApi.Put): FormInstanceQuestionRep.Complete {
        HasAccessToFormInstance(formInstanceService, command.formInstanceId).authorize()
        val model = formInstanceQuestionMapper.model(command.questionId, command.rep.required())
        formInstanceQuestionService.upsert(
            formInstanceId = command.formInstanceId,
            model = model
        )
        return formInstanceQuestionMapper.completeRep(model)
    }
}
