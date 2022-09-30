package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.api.HealthCheckApi as Api

internal class GetHealthCheckLiveness
@Inject constructor() : RestEndpointHandler<Api.GetLiveness, Unit>(Api.GetLiveness::class) {
  override suspend fun handler(endpoint: Api.GetLiveness): Unit = Unit
}
