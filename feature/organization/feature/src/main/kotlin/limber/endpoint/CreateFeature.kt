package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.FeatureService
import limber.api.FeatureApi as Api
import limber.rep.FeatureRep as Rep

public class CreateFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    return featureService.create(endpoint.organizationGuid, endpoint.body)
  }
}
