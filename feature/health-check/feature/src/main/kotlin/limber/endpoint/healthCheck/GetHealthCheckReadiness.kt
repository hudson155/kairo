package limber.endpoint.healthCheck

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import limber.feature.rest.RestEndpointHandler
import limber.rep.healthCheck.HealthCheckRep
import limber.service.healthCheck.HealthCheckService
import limber.api.healthCheck.HealthCheckApi as Api
import limber.rep.healthCheck.HealthCheckRep as Rep

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