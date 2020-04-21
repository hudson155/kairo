package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormInstance
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
) : LimberApiEndpoint<FormInstanceQuestionApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceQuestionApi.Delete::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceQuestionApi.Delete(
        formInstanceId = call.parameters.getAsType(UUID::class, "formInstanceId"),
        questionId = call.parameters.getAsType(UUID::class, "questionId")
    )

    override suspend fun Handler.handle(command: FormInstanceQuestionApi.Delete) {
        HasAccessToFormInstance(formInstanceService, command.formInstanceId).authorize()
        formInstanceQuestionService.delete(command.formInstanceId, command.questionId)
    }
}
