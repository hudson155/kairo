package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exporter.formInstance.FormInstanceExporter
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.client.feature.FeatureClient
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.client.account.UserClient
import io.limberapp.common.finder.SortableFinder
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.common.restInterface.template
import java.time.ZoneId
import java.util.*

internal class ExportFormInstancesByFeatureGuid @Inject constructor(
    application: Application,
    private val formInstanceService: FormInstanceService,
    private val featureClient: FeatureClient,
    private val userClient: UserClient,
) : LimberApiEndpoint<FormInstanceApi.ExportByFeatureGuid, String>(
    application = application,
    endpointTemplate = FormInstanceApi.ExportByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.ExportByFeatureGuid(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      creatorAccountGuid = call.parameters.getAsType(UUID::class, "creatorAccountGuid", optional = true),
      timeZone = call.parameters.getAsType(ZoneId::class, "timeZone", optional = true)
  )

  override suspend fun Handler.handle(command: FormInstanceApi.ExportByFeatureGuid): String {
    auth {
      val onlyRequestingOwnFormInstances = command.creatorAccountGuid?.let { it == principal?.userGuid } == true
      Auth.All(
          AuthFeatureMember(command.featureGuid, permission = FormsFeaturePermission.EXPORT_FORM_INSTANCES),
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
    val formInstances = formInstanceService.findAsList {
      featureGuid(command.featureGuid)
      command.creatorAccountGuid?.let { creatorAccountGuid(it) }
      sortBy(FormInstanceFinder.SortBy.NUMBER, SortableFinder.SortDirection.ASCENDING)
    }
    val feature = checkNotNull(featureClient(FeatureApi.Get(command.featureGuid)))
    val users = userClient(UserApi.GetByOrgGuid(feature.orgGuid))
    return FormInstanceExporter(
        users = users.associateBy { it.guid },
        timeZone = command.timeZone,
        formInstances = formInstances
    ).export()
  }
}
