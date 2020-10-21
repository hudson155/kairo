package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class PatchFormTemplateQuestion @Inject constructor(
    application: Application,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper,
) : LimberApiEndpoint<FormTemplateQuestionApi.Patch, FormTemplateQuestionRep.Complete>(
    application = application,
    endpointTemplate = FormTemplateQuestionApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateQuestionApi.Patch(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid"),
      questionGuid = call.parameters.getAsType(UUID::class, "questionGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormTemplateQuestionApi.Patch): FormTemplateQuestionRep.Complete {
    val rep = command.rep.required()
    auth { AuthFeatureMember(command.featureGuid, permission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES) }
    val formTemplateQuestion = formTemplateQuestionService.update(
        featureGuid = command.featureGuid,
        formTemplateGuid = command.formTemplateGuid,
        questionGuid = command.questionGuid,
        update = formTemplateQuestionMapper.update(rep)
    )
    return formTemplateQuestionMapper.completeRep(formTemplateQuestion)
  }
}
