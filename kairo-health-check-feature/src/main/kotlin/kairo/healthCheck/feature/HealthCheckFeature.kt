package kairo.healthCheck.feature

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kairo.feature.Feature
import kairo.rest.feature.RestFeature

public class HealthCheckFeature : Feature(), RestFeature.HasRouting {
  override val name: String = "Health Check"

  override fun Routing.routing() {
    get("/health/liveness") {
      call.response.status(HttpStatusCode.OK)
    }
    get("/health/readiness") {
      call.response.status(HttpStatusCode.OK)
      call.respondText("OK")
    }
  }
}
