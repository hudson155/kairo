package io.limberapp.endpoint.healthCheck

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.healthCheck.HealthCheckApi
import io.limberapp.exception.healthCheck.HealthCheckFailed
import io.limberapp.testing.integration.IntegrationTest
import io.limberapp.rep.healthCheck.HealthCheckRep
import io.limberapp.server.Server
import io.limberapp.service.healthCheck.TestHealthCheckService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

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
    test(expectError = HealthCheckFailed("Fake Check health check failed.", null)) {
      healthCheckClient(HealthCheckApi.Get)
    }
  }
}
