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
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetFormTemplatesByFeatureGuid @Inject constructor(
    application: Application,
    private val formTemplateService: FormTemplateService,
    private val formTemplateMapper: FormTemplateMapper,
) : LimberApiEndpoint<FormTemplateApi.GetByFeatureGuid, Set<FormTemplateRep.Summary>>(
    application = application,
    endpointTemplate = FormTemplateApi.GetByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.GetByFeatureGuid(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
  )

  override suspend fun Handler.handle(command: FormTemplateApi.GetByFeatureGuid): Set<FormTemplateRep.Summary> {
    auth { AuthFeatureMember(command.featureGuid) }
    val formTemplates = formTemplateService.getByFeatureGuid(command.featureGuid)
    return formTemplates.map { formTemplateMapper.summaryRep(it) }.toSet()
  }
}
