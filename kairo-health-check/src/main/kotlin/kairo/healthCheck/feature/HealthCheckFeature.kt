package kairo.healthCheck.feature

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import kairo.feature.Feature
import kairo.rest.RestFeature

public class HealthCheckFeature : Feature(), RestFeature.HasRouting {
  override val name: String = "Health Check"

  override fun Application.routing() {
    routing {
      get<HealthResource.Liveness> {
        call.response.status(HttpStatusCode.OK)
      }
      get<HealthResource.Readiness> {
        call.response.status(HttpStatusCode.OK)
        call.respondText("OK")
      }
    }
  }
}
