package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.authorization.authorization.AuthFormInstance
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.exception.unprocessableEntity.unprocessable
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
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
    auth { AuthFeatureMember(command.featureGuid) }
    auth {
      AuthFormInstance(
          formInstance = formInstanceService.findOnlyOrNull {
            featureGuid(command.featureGuid)
            formInstanceGuid(command.formInstanceGuid)
          } ?: throw FormInstanceNotFound().unprocessable(),
          ifIsOwnFormInstance = FormsFeaturePermission.MODIFY_OWN_FORM_INSTANCES,
          ifIsNotOwnFormInstance = FormsFeaturePermission.MODIFY_OTHERS_FORM_INSTANCES,
      )
    }
    formInstanceQuestionService.delete(command.featureGuid, command.formInstanceGuid, command.questionGuid)
  }
}
