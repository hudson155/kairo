package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteFormInstanceQuestion @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
) : LimberApiEndpoint<FormInstanceQuestionApi.Delete, Unit>(
    application = application,
    endpointTemplate = FormInstanceQuestionApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceQuestionApi.Delete(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid"),
      questionGuid = call.parameters.getAsType(UUID::class, "questionGuid")
  )

  override suspend fun Handler.handle(command: FormInstanceQuestionApi.Delete) {
    val formInstance = formInstanceService.findOnlyOrNull {
      featureGuid(command.featureGuid)
      formInstanceGuid(command.formInstanceGuid)
    } ?: throw FormInstanceQuestionNotFound()
    Authorization.FeatureMemberWithFeaturePermission(
        featureGuid = command.featureGuid,
        featurePermission = when (formInstance.creatorAccountGuid) {
          principal?.user?.guid -> FormsFeaturePermission.MODIFY_OWN_FORM_INSTANCES
          else -> FormsFeaturePermission.MODIFY_OTHERS_FORM_INSTANCES
        }
    ).authorize()
    if (formInstance.submittedDate == null) Authorization.User(formInstance.creatorAccountGuid).authorize()
    formInstanceQuestionService.delete(command.featureGuid, command.formInstanceGuid, command.questionGuid)
  }
}
