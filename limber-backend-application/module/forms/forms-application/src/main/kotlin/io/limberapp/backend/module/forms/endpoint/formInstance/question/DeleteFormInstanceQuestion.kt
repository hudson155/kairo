package io.limberapp.backend.module.forms.endpoint.formInstance.question

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import java.util.*

/**
 * Deletes a existing question (answer) from a form instance.
 */
internal class DeleteFormInstanceQuestion @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formInstanceQuestionService: FormInstanceQuestionService
) : LimberApiEndpoint<FormInstanceQuestionApi.Delete, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormInstanceQuestionApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceQuestionApi.Delete(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid"),
    questionGuid = call.parameters.getAsType(UUID::class, "questionGuid")
  )

  override suspend fun Handler.handle(command: FormInstanceQuestionApi.Delete) {
    Authorization.FeatureMember(command.featureGuid).authorize()
    formInstanceQuestionService.delete(command.featureGuid, command.formInstanceGuid, command.questionGuid)
  }
}
