package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Returns all form templates within the feature.
 */
internal class GetFormTemplatesByFeatureGuid @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formTemplateService: FormTemplateService,
  private val formTemplateMapper: FormTemplateMapper
) : LimberApiEndpoint<FormTemplateApi.GetByFeatureGuid, Set<FormTemplateRep.Summary>>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormTemplateApi.GetByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.GetByFeatureGuid(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
  )

  override suspend fun Handler.handle(command: FormTemplateApi.GetByFeatureGuid): Set<FormTemplateRep.Summary> {
    Authorization.HasAccessToFeature(command.featureGuid).authorize()
    val formTemplates = formTemplateService.getByFeatureGuid(command.featureGuid)
    return formTemplates.map { formTemplateMapper.summaryRep(it) }.toSet()
  }
}
