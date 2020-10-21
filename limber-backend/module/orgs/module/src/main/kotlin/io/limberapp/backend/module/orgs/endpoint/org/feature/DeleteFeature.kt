package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthOrgMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteFeature @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
) : LimberApiEndpoint<FeatureApi.Delete, Unit>(
    application = application,
    endpointTemplate = FeatureApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
  )

  override suspend fun Handler.handle(command: FeatureApi.Delete) {
    auth { AuthOrgMember(command.orgGuid, permission = OrgPermission.MANAGE_ORG_FEATURES) }
    featureService.delete(
        orgGuid = command.orgGuid,
        featureGuid = command.featureGuid
    )
  }
}
