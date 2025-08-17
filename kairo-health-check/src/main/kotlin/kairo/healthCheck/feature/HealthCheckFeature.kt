package kairo.healthCheck.feature

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import kairo.feature.Feature
import kairo.rest.RestFeature

/**
 * The Health Check Feature binds 2 routes for health checks.
 * The liveness check always immediately responds with 200 OK.
 * The readiness check responds with 200 OK only after a series of checks passes. // TODO: Explain checks.
 */
// TODO: Make configurable.
public class HealthCheckFeature : Feature(), RestFeature.HasRouting {
  override val name: String = "Health Check"

  // TODO: This inline format is not good (use handler).
  // TODO: The response types are not good.
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
