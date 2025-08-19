package kairo.healthCheck.feature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kairo.rest.RestFeature
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private val logger: KLogger = KotlinLogging.logger {}

internal class HealthCheckHandler(
  private val healthChecks: Map<String, HealthCheck>, // TODO: Add default (server is running) health check.
) : RestFeature.HasRouting {
  override fun Application.routing() {
    routing {
      get<HealthCheckApi.Liveness> {
        // The liveness check immediately returns 200 OK.
        call.respond(HttpStatusCode.OK)
      }
      get<HealthCheckApi.Readiness> {
        // Run the health checks in parallel.
        val checks = coroutineScope {
          healthChecks.map { (name, healthCheck) ->
            async(Dispatchers.IO) {
              val success = try {
                healthCheck.check()
                true
              } catch (e: CancellationException) {
                throw e
              } catch (e: Exception) {
                logger.warn(e) { "Health check failed (name=$name)." }
                false
              }
              return@async Pair(name, success)
            }
          }.awaitAll().toMap()
        }
        val success = checks.all { (_, value) -> value }
        call.respond(
          message = HealthCheckRep(success, checks),
          status = if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError,
        )
      }
    }
  }
}
