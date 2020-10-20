package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.authorization.authorization.AuthUser
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import java.util.*

internal class PostFormInstance @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
    private val formInstanceMapper: FormInstanceMapper,
) : LimberApiEndpoint<FormInstanceApi.Post, FormInstanceRep.Complete>(
    application = application,
    endpointTemplate = FormInstanceApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Post(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FormInstanceApi.Post): FormInstanceRep.Complete {
    val rep = command.rep.required()
    auth {
      Auth.All(
          AuthFeatureMember(command.featureGuid, permission = FormsFeaturePermission.CREATE_FORM_INSTANCES),
          AuthUser(rep.creatorAccountGuid),
      )
    }
    val formInstance = formInstanceService.create(formInstanceMapper.model(command.featureGuid, rep))
    val questions = formInstanceQuestionService.getByFormInstanceGuid(
        featureGuid = command.featureGuid,
        formInstanceGuid = formInstance.guid
    )
    return formInstanceMapper.completeRep(formInstance, questions)
  }
}
