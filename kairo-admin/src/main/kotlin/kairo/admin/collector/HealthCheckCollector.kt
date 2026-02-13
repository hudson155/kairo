package kairo.admin.collector

import kairo.admin.model.HealthCheckResult
import kotlin.system.measureTimeMillis

internal class HealthCheckCollector(
  private val healthChecks: Map<String, suspend () -> Unit>,
) {
  val hasChecks: Boolean get() = healthChecks.isNotEmpty()

  suspend fun runAll(): List<HealthCheckResult> =
    healthChecks.map { (name, check) -> runSingle(name, check) }

  suspend fun run(name: String): HealthCheckResult? {
    val check = healthChecks[name] ?: return null
    return runSingle(name, check)
  }

  private suspend fun runSingle(name: String, check: suspend () -> Unit): HealthCheckResult {
    var error: String? = null
    val durationMs = measureTimeMillis {
      try {
        check()
      } catch (e: Exception) {
        error = e.message ?: e::class.simpleName.orEmpty()
      }
    }
    return HealthCheckResult(
      name = name,
      passed = error == null,
      durationMs = durationMs,
      error = error,
    )
  }
}
