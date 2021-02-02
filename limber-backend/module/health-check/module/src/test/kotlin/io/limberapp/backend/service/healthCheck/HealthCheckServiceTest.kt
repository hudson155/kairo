package io.limberapp.backend.service.healthCheck

import io.limberapp.backend.model.healthCheck.HealthCheckModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class HealthCheckServiceTest {
  @Test
  fun `healthy (trivial case)`() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override fun healthCheck(): HealthCheckModel = HealthCheckModel.Healthy
    }
    assertEquals(HealthCheckModel.Healthy, healthCheckService.healthCheck())
  }

  @Test
  fun `unhealthy (trivial case)`() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override fun healthCheck(): HealthCheckModel = HealthCheckModel.Unhealthy("abcde is why!")
    }
    assertEquals(HealthCheckModel.Unhealthy("abcde is why!"), healthCheckService.healthCheck())
  }

  @Test
  fun `healthy (no exception thrown)`() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override fun healthCheck(): HealthCheckModel {
        healthTry("first check") {}
        return HealthCheckModel.Healthy
      }
    }
    assertEquals(HealthCheckModel.Healthy, healthCheckService.healthCheck())
  }

  @Test
  fun `unhealthy (exception thrown)`() {
    val healthCheckService: HealthCheckService = object : HealthCheckService() {
      override fun healthCheck(): HealthCheckModel {
        healthTry("First Check") {
          throw RuntimeException("something bad :'(")
        }?.let { return@healthCheck it }
        return HealthCheckModel.Healthy
      }
    }
    healthCheckService.healthCheck().let {
      assertTrue(it is HealthCheckModel.Unhealthy)
      assertEquals("First Check health check failed.", it.reason)
      assertEquals("something bad :'(", it.e?.message)
    }
  }
}
