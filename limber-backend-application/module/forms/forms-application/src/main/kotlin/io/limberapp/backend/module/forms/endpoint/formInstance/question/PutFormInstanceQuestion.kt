package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.*

internal class PutFormInstanceQuestion @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formInstanceService: FormInstanceService,
  private val formInstanceQuestionService: FormInstanceQuestionService,
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
) : LimberApiEndpoint<FormInstanceQuestionApi.Put, FormInstanceQuestionRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormInstanceQuestionApi.Put::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceQuestionApi.Put(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid"),
    questionGuid = call.parameters.getAsType(UUID::class, "questionGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormInstanceQuestionApi.Put): FormInstanceQuestionRep.Complete {
    val rep = command.rep.required()
    val formInstance = formInstanceService.get(command.featureGuid, command.formInstanceGuid)
      ?: throw FormInstanceNotFound()
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = when (formInstance.creatorAccountGuid) {
        principal?.user?.guid -> FormsFeaturePermission.MODIFY_OWN_FORM_INSTANCES
        else -> FormsFeaturePermission.MODIFY_OTHERS_FORM_INSTANCES
      }
    ).authorize()
    val formInstanceQuestion = formInstanceQuestionService.upsert(
      featureGuid = command.featureGuid,
      model = formInstanceQuestionMapper.model(command.formInstanceGuid, command.questionGuid, rep)
    )
    return formInstanceQuestionMapper.completeRep(formInstanceQuestion)
  }
}
