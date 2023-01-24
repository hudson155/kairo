package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
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
    auth(
      auth = OrganizationAuth(
        organizationGuid = { featureService.get(endpoint.organizationGuid, endpoint.featureGuid)?.organizationGuid },
        permission = OrganizationPermission.FeatureUpdate,
      ),
      onFail = { throw FeatureDoesNotExist() },
    )

    return featureService.update(endpoint.organizationGuid, endpoint.featureGuid, getBody(endpoint))
  }
}
