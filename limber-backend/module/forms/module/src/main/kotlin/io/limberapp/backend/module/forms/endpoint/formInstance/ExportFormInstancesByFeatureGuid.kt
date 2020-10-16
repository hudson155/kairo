package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exporter.formInstance.FormInstanceExporter
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.client.account.UserClient
import io.limberapp.common.finder.SortableFinder
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import java.time.ZoneId
import java.util.*

internal class ExportFormInstancesByFeatureGuid @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
    private val formInstanceService: FormInstanceService,
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
    Authorization.FeatureMemberWithFeaturePermission(
        featureGuid = command.featureGuid,
        featurePermission = FormsFeaturePermission.EXPORT_FORM_INSTANCES
    ).authorize()
    if (command.creatorAccountGuid == null || command.creatorAccountGuid != principal?.userGuid) {
      Authorization.FeatureMemberWithFeaturePermission(
          featureGuid = command.featureGuid,
          featurePermission = FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES
      ).authorize()
    }
    val formInstances = formInstanceService.findAsList {
      featureGuid(command.featureGuid)
      command.creatorAccountGuid?.let { creatorAccountGuid(it) }
      sortBy(FormInstanceFinder.SortBy.NUMBER, SortableFinder.SortDirection.ASCENDING)
    }
    val feature = featureService.findOnlyOrThrow { featureGuid(command.featureGuid) }
    val users = userClient(UserApi.GetByOrgGuid(feature.orgGuid))
    return FormInstanceExporter(
        users = users.associateBy { it.guid },
        timeZone = command.timeZone,
        formInstances = formInstances
    ).export()
  }
}
