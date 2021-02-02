package io.limberapp.backend.service.healthCheck

import io.limberapp.backend.model.healthCheck.HealthCheckModel

/**
 * This health check implementation alternates indicating healthy and unhealthy, starting with
 * healthy. Not very useful in the real world, but pretty useful for tests.
 */
internal class TestHealthCheckService : HealthCheckService() {
  private var healthy: Boolean = false // Note: Not thread-safe.
  override fun healthCheck(): HealthCheckModel {
    healthy = !healthy
    healthTry("fake check") {
      if (!healthy) throw RuntimeException("Not healthy... maybe next time.")
    }?.let { return@healthCheck it }
    return HealthCheckModel.Healthy
  }
}
