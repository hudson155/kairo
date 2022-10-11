package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.FeatureService
import limber.api.FeatureApi as Api
import limber.rep.FeatureRep as Rep

public class DeleteFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    return featureService.delete(endpoint.organizationGuid, endpoint.featureGuid)
  }
}
