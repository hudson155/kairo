package limber.endpoint

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import limber.rep.HealthCheckRep
import limber.rest.RestEndpointHandler
import limber.service.HealthCheckService
import limber.api.HealthCheckApi as Api
import limber.rep.HealthCheckRep as Rep

internal class GetHealthCheckReadiness @Inject constructor(
  private val healthCheckService: HealthCheckService,
) : RestEndpointHandler<Api.GetReadiness, Rep>(Api.GetReadiness::class) {
  override suspend fun handler(endpoint: Api.GetReadiness): Rep =
    healthCheckService.healthCheck()

  override fun status(result: HealthCheckRep): HttpStatusCode {
    if (result.state != HealthCheckRep.State.Healthy) return HttpStatusCode.InternalServerError
    return super.status(result)
  }
}
