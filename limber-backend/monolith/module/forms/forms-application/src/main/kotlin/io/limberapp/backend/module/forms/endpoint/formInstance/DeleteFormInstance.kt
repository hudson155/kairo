package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
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
    val formInstance = formInstanceService.findOnlyOrNull {
      featureGuid(command.featureGuid)
      formInstanceGuid(command.formInstanceGuid)
    } ?: throw FormInstanceNotFound()
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = when (formInstance.creatorAccountGuid) {
        principal?.user?.guid -> FormsFeaturePermission.DELETE_OWN_FORM_INSTANCES
        else -> FormsFeaturePermission.DELETE_OTHERS_FORM_INSTANCES
      }
    ).authorize()
    formInstanceService.delete(command.featureGuid, command.formInstanceGuid)
  }
}
