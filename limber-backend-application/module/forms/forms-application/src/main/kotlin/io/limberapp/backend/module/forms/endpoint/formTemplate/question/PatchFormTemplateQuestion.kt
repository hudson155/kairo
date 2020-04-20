package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormTemplate
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Updates a form template question's information.
 */
internal class PatchFormTemplateQuestion @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : LimberApiEndpoint<FormTemplateQuestionApi.Patch, FormTemplateQuestionRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormTemplateQuestionApi.Patch::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = FormTemplateQuestionApi.Patch(
        formTemplateId = call.parameters.getAsType(UUID::class, "formTemplateId"),
        questionId = call.parameters.getAsType(UUID::class, "questionId"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: FormTemplateQuestionApi.Patch): FormTemplateQuestionRep.Complete {
        HasAccessToFormTemplate(formTemplateService, command.formTemplateId).authorize()
        val update = formTemplateQuestionMapper.update(command.rep.required())
        val model = formTemplateQuestionService.update(command.formTemplateId, command.questionId, update)
        return formTemplateQuestionMapper.completeRep(model)
    }
}
