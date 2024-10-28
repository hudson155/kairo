package kairo.healthCheck

import com.google.inject.Inject
import io.ktor.http.HttpStatusCode
import kairo.rest.auth.Auth
import kairo.rest.auth.AuthProvider
import kairo.rest.auth.public
import kairo.rest.handler.RestHandler

@Suppress("UseDataClass") // Handlers shouldn't be data classes.
internal class HealthCheckHandler @Inject constructor(
  private val healthCheckService: HealthCheckService,
) {
  internal inner class Liveness : RestHandler<HealthCheckApi.Liveness, Unit>() {
    override suspend fun AuthProvider.auth(endpoint: HealthCheckApi.Liveness): Auth.Result =
      public()

    override suspend fun handle(endpoint: HealthCheckApi.Liveness) {
      healthCheckService.liveness()
    }
  }

  internal inner class Readiness : RestHandler<HealthCheckApi.Readiness, HealthCheckRep>() {
    override suspend fun AuthProvider.auth(endpoint: HealthCheckApi.Readiness): Auth.Result =
      public()

    override suspend fun handle(endpoint: HealthCheckApi.Readiness): HealthCheckRep =
      healthCheckService.readiness()

    override fun statusCode(response: HealthCheckRep): HttpStatusCode {
      if (response.status != HealthCheckRep.Status.Healthy) {
        return HttpStatusCode.InternalServerError
      }
      return super.statusCode(response)
    }
  }
}
