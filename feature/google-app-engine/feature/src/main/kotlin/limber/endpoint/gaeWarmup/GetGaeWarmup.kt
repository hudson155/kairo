package limber.endpoint.gaeWarmup

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import limber.auth.Auth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.rep.healthCheck.HealthCheckRep
import limber.service.healthCheck.HealthCheckService
import limber.api.gaeWarmup.GaeWarmupApi as Api
import limber.rep.healthCheck.HealthCheckRep as Rep

internal class GetGaeWarmup @Inject constructor(
  private val healthCheckService: HealthCheckService,
) : RestEndpointHandler<Api.Get, Rep>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep {
    auth { Auth.Public }

    return healthCheckService.healthCheck()
  }

  override fun status(result: Rep): HttpStatusCode {
    if (result.state != HealthCheckRep.State.Healthy) return HttpStatusCode.InternalServerError
    return super.status(result)
  }
}
