package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
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
    Authorization.FeatureMember(command.featureGuid).authorize()
    val formInstance = formInstanceService.findOnlyOrNull {
      featureGuid(command.featureGuid)
      formInstanceGuid(command.formInstanceGuid)
    } ?: throw FormInstanceNotFound()
    if (formInstance.creatorAccountGuid != principal?.userGuid) {
      Authorization.FeatureMemberWithFeaturePermission(
          featureGuid = command.featureGuid,
          featurePermission = FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES
      ).authorize()
    }
    val questions = formInstanceQuestionService.getByFormInstanceGuid(
        featureGuid = command.featureGuid,
        formInstanceGuid = command.formInstanceGuid
    )
    return formInstanceMapper.completeRep(formInstance, questions)
  }
}
