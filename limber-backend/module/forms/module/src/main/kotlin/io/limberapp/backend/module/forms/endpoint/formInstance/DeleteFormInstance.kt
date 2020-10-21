package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.authorization.authorization.AuthFormInstance
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteFormInstance @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
) : LimberApiEndpoint<FormInstanceApi.Delete, Unit>(
    application = application,
    endpointTemplate = FormInstanceApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Delete(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid")
  )

  override suspend fun Handler.handle(command: FormInstanceApi.Delete) {
    auth { AuthFeatureMember(command.featureGuid) }
    auth {
      AuthFormInstance(
          formInstance = formInstanceService.findOnlyOrNull {
            featureGuid(command.featureGuid)
            formInstanceGuid(command.formInstanceGuid)
          } ?: throw FormInstanceNotFound(),
          ifIsOwnFormInstance = FormsFeaturePermission.DELETE_OWN_FORM_INSTANCES,
          ifIsNotOwnFormInstance = FormsFeaturePermission.DELETE_OTHERS_FORM_INSTANCES,
      )
    }
    formInstanceService.delete(command.featureGuid, command.formInstanceGuid)
  }
}
