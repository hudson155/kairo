package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormTemplate
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

/**
 * Deletes an existing form template.
 */
internal class DeleteFormTemplate @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formTemplateService: FormTemplateService
) : LimberApiEndpoint<FormTemplateApi.Delete, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormTemplateApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.Delete(
    formTemplateGuid = call.parameters.getAsType(UUID::class, "formTemplateGuid")
  )

  override suspend fun Handler.handle(command: FormTemplateApi.Delete) {
    HasAccessToFormTemplate(formTemplateService, command.formTemplateGuid).authorize()
    formTemplateService.delete(command.formTemplateGuid)
  }
}
