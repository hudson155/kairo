package limber.endpoint.healthCheck

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.api.healthCheck.HealthCheckApi as Api

internal class GetHealthCheckLiveness
@Inject constructor() : RestEndpointHandler<Api.GetLiveness, Unit>(Api.GetLiveness::class) {
  override suspend fun handler(endpoint: Api.GetLiveness): Unit = Unit
}
