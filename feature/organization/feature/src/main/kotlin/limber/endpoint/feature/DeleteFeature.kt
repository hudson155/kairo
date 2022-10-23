package limber.endpoint.feature

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class DeleteFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    return featureService.delete(endpoint.organizationGuid, endpoint.featureGuid)
  }
}
