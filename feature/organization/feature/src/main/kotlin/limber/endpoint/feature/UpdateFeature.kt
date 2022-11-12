package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.exception.feature.FeatureDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class UpdateFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.FeatureUpdate))
    auth(OrganizationAuth(endpoint.organizationGuid)) { throw FeatureDoesNotExist() }
    return featureService.update(endpoint.organizationGuid, endpoint.featureGuid, endpoint.body)
  }
}
