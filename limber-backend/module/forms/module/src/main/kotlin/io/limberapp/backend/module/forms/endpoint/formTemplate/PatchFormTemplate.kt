package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class PatchFormTemplate @Inject constructor(
    application: Application,
    private val formTemplateService: FormTemplateService,
    private val formTemplateMapper: FormTemplateMapper,
) : LimberApiEndpoint<FormTemplateApi.Patch, FormTemplateRep.Summary>(
    application = application,
    endpointTemplate = FormTemplateApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.Patch(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormTemplateApi.Patch): FormTemplateRep.Summary {
    val rep = command.rep.required()
    auth { AuthFeatureMember(command.featureGuid, permission = FormsFeaturePermission.MANAGE_FORM_TEMPLATES) }
    val formTemplate = formTemplateService.update(
        featureGuid = command.featureGuid,
        formTemplateGuid = command.formTemplateGuid,
        update = formTemplateMapper.update(rep)
    )
    return formTemplateMapper.summaryRep(formTemplate)
  }
}
