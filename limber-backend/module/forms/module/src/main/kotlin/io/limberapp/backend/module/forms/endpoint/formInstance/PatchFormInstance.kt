package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.authorization.authorization.AuthFormInstance
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import java.util.*

internal class PatchFormInstance @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper,
) : LimberApiEndpoint<FormInstanceApi.Patch, FormInstanceRep.Summary>(
    application = application,
    endpointTemplate = FormInstanceApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Patch(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormInstanceApi.Patch): FormInstanceRep.Summary {
    val rep = command.rep.required()
    auth { AuthFeatureMember(command.featureGuid) }
    auth {
      AuthFormInstance(
          formInstance = formInstanceService.findOnlyOrNull {
            featureGuid(command.featureGuid)
            formInstanceGuid(command.formInstanceGuid)
          } ?: throw FormInstanceNotFound(),
          ifIsOwnFormInstance = FormsFeaturePermission.CREATE_FORM_INSTANCES,
          ifIsNotOwnFormInstance = null,
      )
    }
    val formInstance = formInstanceService.update(
        featureGuid = command.featureGuid,
        formInstanceGuid = command.formInstanceGuid,
        update = formInstanceMapper.update(rep)
    )
    return formInstanceMapper.summaryRep(formInstance)
  }
}
