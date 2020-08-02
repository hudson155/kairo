package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import com.piperframework.types.TimeZone
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.LimberModule
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.exporter.formInstance.FormInstanceExporter
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.users.service.account.UserService
import java.util.*

@OptIn(LimberModule.Orgs::class, LimberModule.Users::class)
internal class ExportFormInstancesByFeatureGuid @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val featureService: FeatureService,
  private val formInstanceService: FormInstanceService,
  private val userService: UserService
) : LimberApiEndpoint<FormInstanceApi.ExportByFeatureGuid, String>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = FormInstanceApi.ExportByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.ExportByFeatureGuid(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    creatorAccountGuid = call.parameters.getAsType(UUID::class, "creatorAccountGuid", optional = true),
    timeZone = call.parameters.getAsType(TimeZone::class, "timeZone", optional = true)
  )

  override suspend fun Handler.handle(command: FormInstanceApi.ExportByFeatureGuid): String {
    Authorization.FeatureMemberWithFeaturePermission(
      featureGuid = command.featureGuid,
      featurePermission = FormsFeaturePermission.EXPORT_FORM_INSTANCES
    ).authorize()
    if (command.creatorAccountGuid == null || command.creatorAccountGuid != principal?.user?.guid) {
      Authorization.FeatureMemberWithFeaturePermission(
        featureGuid = command.featureGuid,
        featurePermission = FormsFeaturePermission.SEE_OTHERS_FORM_INSTANCES
      ).authorize()
    }
    val formInstances = formInstanceService.getByFeatureGuid(command.featureGuid, command.creatorAccountGuid)
    val feature = checkNotNull(featureService.getByFeatureGuid(command.featureGuid))
    val users = userService.getByOrgGuid(feature.orgGuid)
    return FormInstanceExporter(
      users = users.associateBy { it.guid },
      timeZone = command.timeZone,
      formInstances = formInstances
    ).export()
  }
}
