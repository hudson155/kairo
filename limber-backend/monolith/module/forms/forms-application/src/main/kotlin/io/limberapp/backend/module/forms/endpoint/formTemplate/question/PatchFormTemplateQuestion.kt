package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
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
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES
    ).authorize()
    val formTemplateQuestion = formTemplateQuestionService.update(
      featureGuid = command.featureGuid,
      formTemplateGuid = command.formTemplateGuid,
      questionGuid = command.questionGuid,
      update = formTemplateQuestionMapper.update(rep)
    )
    return formTemplateQuestionMapper.completeRep(formTemplateQuestion)
  }
}
