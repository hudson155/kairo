package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import java.util.*

internal class DeleteFormTemplate @Inject constructor(
    application: Application,
    private val formTemplateService: FormTemplateService,
) : LimberApiEndpoint<FormTemplateApi.Delete, Unit>(
    application = application,
    endpointTemplate = FormTemplateApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.Delete(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid")
  )

  override suspend fun Handler.handle(command: FormTemplateApi.Delete) {
    Authorization.FeatureMemberWithFeaturePermission(
        featureGuid = command.featureGuid,
        featurePermission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES
    ).authorize()
    formTemplateService.delete(command.featureGuid, command.formTemplateGuid)
  }
}
