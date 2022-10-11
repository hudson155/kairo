package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.FeatureService
import limber.api.FeatureApi as Api
import limber.rep.FeatureRep as Rep

public class GetFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    return featureService.get(endpoint.organizationGuid, endpoint.featureGuid)
  }
}
