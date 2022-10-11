package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.FeatureService
import limber.api.FeatureApi as Api
import limber.rep.FeatureRep as Rep

public class UpdateFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    return featureService.update(endpoint.organizationGuid, endpoint.featureGuid, endpoint.body)
  }
}
