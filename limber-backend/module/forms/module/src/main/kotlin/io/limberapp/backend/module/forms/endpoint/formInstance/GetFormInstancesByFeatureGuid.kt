package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.common.finder.SortableFinder
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import java.util.*

internal class GetFormInstancesByFeatureGuid @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper,
) : LimberApiEndpoint<FormInstanceApi.GetByFeatureGuid, List<FormInstanceRep.Summary>>(
    application = application,
    endpointTemplate = FormInstanceApi.GetByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.GetByFeatureGuid(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      creatorAccountGuid = call.parameters.getAsType(UUID::class, "creatorAccountGuid", optional = true)
  )

  override suspend fun Handler.handle(command: FormInstanceApi.GetByFeatureGuid): List<FormInstanceRep.Summary> {
    auth {
      val onlyRequestingOwnFormInstances = command.creatorAccountGuid?.let { it == principal?.userGuid } == true
      Auth.All(
          AuthFeatureMember(command.featureGuid),
          Auth.Conditional(
              on = onlyRequestingOwnFormInstances,
              ifTrue = Auth.Allow,
              ifFalse = AuthFeatureMember(
                  featureGuid = command.featureGuid,
                  permission = FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES,
              ),
          ),
      )
    }
    val formInstances = formInstanceService.findAsSet {
      featureGuid(command.featureGuid)
      command.creatorAccountGuid?.let { creatorAccountGuid(it) }
      sortBy(FormInstanceFinder.SortBy.NUMBER, SortableFinder.SortDirection.DESCENDING)
    }
    return formInstances.map { formInstanceMapper.summaryRep(it) }
  }
}
