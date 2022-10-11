package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.FeatureService
import limber.api.FeatureApi as Api
import limber.rep.FeatureRep as Rep

public class GetFeaturesByOrganization @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.GetByOrganization, List<Rep>>(Api.GetByOrganization::class) {
  override suspend fun handler(endpoint: Api.GetByOrganization): List<Rep> {
    return featureService.getByOrganization(endpoint.organizationGuid)
  }
}
