package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import java.util.*

internal class DeleteFormTemplateQuestion @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formTemplateQuestionService: FormTemplateQuestionService
) : LimberApiEndpoint<FormTemplateQuestionApi.Delete, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormTemplateQuestionApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateQuestionApi.Delete(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid"),
    questionGuid = call.parameters.getAsType(UUID::class, "questionGuid")
  )

  override suspend fun Handler.handle(command: FormTemplateQuestionApi.Delete) {
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES
    ).authorize()
    formTemplateQuestionService.delete(command.featureGuid, command.formTemplateGuid, command.questionGuid)
  }
}
