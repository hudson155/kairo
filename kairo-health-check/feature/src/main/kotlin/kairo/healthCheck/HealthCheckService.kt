package kairo.healthCheck

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.time.Duration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout

private val logger: KLogger = KotlinLogging.logger {}

internal class HealthCheckService(
  private val healthChecks: Map<String, HealthCheck>,
  private val timeout: Duration,
) {
  fun liveness(): Unit =
    Unit // Yes, this is the intended implementation.

  suspend fun readiness(): HealthCheckRep {
    // Run the health checks in parallel.
    val checks = coroutineScope {
      healthChecks.map { (name, healthCheck) ->
        async { Pair(name, check(name, healthCheck)) }
      }.awaitAll().toMap()
    }
    return HealthCheckRep(
      success = checks.all { (_, value) -> value },
      checks = checks,
    )
  }

  private suspend fun check(name: String, healthCheck: HealthCheck): Boolean {
    try {
      withTimeout(timeout) {
        healthCheck.check()
      }
      return true
    } catch (e: TimeoutCancellationException) {
      logger.warn(e) { "Health check timed out (name=$name)." }
      return false
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      logger.warn(e) { "Health check failed (name=$name)." }
      return false
    }
  }
}
