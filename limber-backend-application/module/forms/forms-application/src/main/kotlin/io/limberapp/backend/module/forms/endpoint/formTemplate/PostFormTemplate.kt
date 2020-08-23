package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.*

internal class PostFormTemplate @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formTemplateService: FormTemplateService,
  private val formTemplateQuestionService: FormTemplateQuestionService,
  private val formTemplateMapper: FormTemplateMapper,
) : LimberApiEndpoint<FormTemplateApi.Post, FormTemplateRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormTemplateApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.Post(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormTemplateApi.Post): FormTemplateRep.Complete {
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES
    ).authorize()
    val formTemplate = formTemplateService.create(formTemplateMapper.model(command.featureGuid, command.rep.required()))
    val questions = formTemplateQuestionService.findAsList {
      featureGuid(command.featureGuid)
      formTemplateGuid(formTemplate.guid)
    }
    return formTemplateMapper.completeRep(formTemplate, questions)
  }
}
