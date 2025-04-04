package kairo.googleAppEngine

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import kairo.healthCheck.HealthCheckRep
import kairo.healthCheck.HealthCheckService
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.auth.public
import kairo.rest.handler.RestHandler

@Suppress("UseDataClass")
internal class GoogleAppEngineHandler @Inject constructor(
  private val healthCheckService: HealthCheckService,
) {
  internal inner class Warmup : RestHandler<GoogleAppEngineApi.Warmup, HealthCheckRep>() {
    override suspend fun AuthProvider.auth(endpoint: GoogleAppEngineApi.Warmup): Auth.Result =
      public()

    override suspend fun handle(endpoint: GoogleAppEngineApi.Warmup): HealthCheckRep =
      healthCheckService.readiness()

    override fun statusCode(response: HealthCheckRep): HttpStatusCode {
      if (response.status != HealthCheckRep.Status.Healthy) {
        return HttpStatusCode.InternalServerError
      }
      return super.statusCode(response)
    }
  }
}
