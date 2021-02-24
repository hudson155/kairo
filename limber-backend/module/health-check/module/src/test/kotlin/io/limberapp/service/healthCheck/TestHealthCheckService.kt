package io.limberapp.service.healthCheck

import com.google.inject.Inject

/**
 * This health check implementation alternates indicating healthy and unhealthy, starting with
 * healthy. Not very useful in the real world, but pretty useful for tests.
 */
internal class TestHealthCheckService @Inject constructor() : HealthCheckService() {
  private var healthy: Boolean = false // Note: Not thread-safe.
  override val healthChecks: List<HealthCheck> = listOf(
      object : HealthCheck("Fake Check") {
        override fun check() {
          healthy = !healthy
          if (!healthy) throw RuntimeException("Not healthy... maybe next time.")
        }
      }
  )
}
