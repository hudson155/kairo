package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
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
import java.util.*

internal class PatchFormInstance @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val formInstanceService: FormInstanceService,
  private val formInstanceQuestionService: FormInstanceQuestionService,
  private val formInstanceMapper: FormInstanceMapper,
) : LimberApiEndpoint<FormInstanceApi.Patch, FormInstanceRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormInstanceApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Patch(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormInstanceApi.Patch): FormInstanceRep.Complete {
    val rep = command.rep.required()
    Authorization.FeatureMember(command.featureGuid).authorize()
    run {
      val formInstance = formInstanceService.get(command.featureGuid, command.formInstanceGuid)
        ?: throw FormInstanceNotFound()
      Authorization.User(formInstance.creatorAccountGuid).authorize()
    }
    val formInstance = formInstanceService.update(
      featureGuid = command.featureGuid,
      formInstanceGuid = command.formInstanceGuid,
      update = formInstanceMapper.update(rep)
    )
    val questions = formInstanceQuestionService.getByFormInstanceGuid(command.featureGuid, formInstance.guid)
    return formInstanceMapper.completeRep(formInstance, questions)
  }
}
