package io.limberapp.service.healthCheck

import io.limberapp.model.healthCheck.HealthCheckModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class HealthCheckServiceTest {
  @Test
  fun healthy() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override val healthChecks: List<HealthCheck> = listOf(
          object : HealthCheck("First Check") {
            override fun check(): Unit = Unit
          }
      )
    }
    assertEquals(HealthCheckModel.Healthy, healthCheckService.healthCheck())
  }

  @Test
  fun unhealthy() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override val healthChecks: List<HealthCheck> = listOf(
          object : HealthCheck("First Check") {
            override fun check(): Nothing = error("abcde is why!")
          }
      )
    }
    healthCheckService.healthCheck().let {
      assertTrue(it is HealthCheckModel.Unhealthy)
      assertEquals("First Check health check failed.", it.reason)
      assertEquals("abcde is why!", it.e?.message)
    }
  }
}
