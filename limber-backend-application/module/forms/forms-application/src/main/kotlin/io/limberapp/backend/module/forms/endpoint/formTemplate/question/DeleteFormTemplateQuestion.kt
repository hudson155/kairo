package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormTemplate
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Deletes a existing question from a form template.
 */
internal class DeleteFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService
) : LimberApiEndpoint<FormTemplateQuestionApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormTemplateQuestionApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormTemplateQuestionApi.Delete(
        formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid"),
        questionGuid = call.parameters.getAsType(UUID::class, "questionGuid")
    )

    override suspend fun Handler.handle(command: FormTemplateQuestionApi.Delete) {
        HasAccessToFormTemplate(formTemplateService, command.formTemplateGuid).authorize()
        formTemplateQuestionService.delete(command.formTemplateGuid, command.questionGuid)
    }
}
