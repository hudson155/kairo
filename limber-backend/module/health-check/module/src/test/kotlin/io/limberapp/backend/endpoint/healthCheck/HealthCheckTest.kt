package io.limberapp.backend.endpoint.healthCheck

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.api.healthCheck.HealthCheckApi
import io.limberapp.backend.exception.HealthCheckFailed
import io.limberapp.backend.module.healthCheck.IntegrationTest
import io.limberapp.backend.rep.healthCheck.HealthCheckRep
import io.limberapp.backend.service.healthCheck.TestHealthCheckService
import io.limberapp.common.server.Server
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.Test

/**
 * These tests must be run in order, because the [TestHealthCheckService] alternates indicating
 * healthy and unhealthy, starting with healthy.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class HealthCheckTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  @Order(1)
  fun healthy() {
    test(expectResult = HealthCheckRep.Complete(HealthCheckRep.State.HEALTHY)) {
      healthCheckClient(HealthCheckApi.Get)
    }
  }

  @Test
  @Order(2)
  fun unhealthy(): Unit = runBlocking {
    test(expectError = HealthCheckFailed("fake check health check failed.", null)) {
      healthCheckClient(HealthCheckApi.Get)
    }
  }
}
