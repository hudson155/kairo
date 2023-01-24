package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class GetFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(
      auth = OrganizationAuth(
        organizationGuid = { featureService.get(endpoint.featureGuid)?.organizationGuid },
        permission = OrganizationPermission.FeatureRead,
      ),
      onFail = { return@handler null },
    )

    return featureService.get(endpoint.featureGuid)
  }
}
