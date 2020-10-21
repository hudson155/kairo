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
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetFormInstance @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
    private val formInstanceMapper: FormInstanceMapper,
) : LimberApiEndpoint<FormInstanceApi.Get, FormInstanceRep.Complete>(
    application = application,
    endpointTemplate = FormInstanceApi.Get::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Get(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid")
  )

  override suspend fun Handler.handle(command: FormInstanceApi.Get): FormInstanceRep.Complete {
    auth { AuthFeatureMember(command.featureGuid) }
    val formInstance = formInstanceService.findOnlyOrNull {
      featureGuid(command.featureGuid)
      formInstanceGuid(command.formInstanceGuid)
    } ?: throw FormInstanceNotFound()
    auth {
      AuthFormInstance(
          formInstance = formInstance,
          ifIsOwnFormInstance = null,
          ifIsNotOwnFormInstance = FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES,
      )
    }
    val questions = formInstanceQuestionService.getByFormInstanceGuid(
        featureGuid = command.featureGuid,
        formInstanceGuid = command.formInstanceGuid
    )
    return formInstanceMapper.completeRep(formInstance, questions)
  }
}
