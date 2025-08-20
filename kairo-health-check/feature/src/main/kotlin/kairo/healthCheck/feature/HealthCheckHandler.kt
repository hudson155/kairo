package kairo.healthCheck.feature

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kairo.rest.RestFeature
import kotlin.time.Duration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout

private val logger: KLogger = KotlinLogging.logger {}

internal class HealthCheckHandler(
  private val healthChecks: Map<String, HealthCheck>, // TODO: Add default (server is running) health check.
  private val timeout: Duration,
) : RestFeature.HasRouting {
  @Suppress("CognitiveComplexMethod")
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
            async {
              val success = try {
                withTimeout(timeout) {
                  healthCheck.check()
                }
                true
              } catch (e: TimeoutCancellationException) {
                logger.warn(e) { "Health check timed out (name=$name)." }
                false
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
