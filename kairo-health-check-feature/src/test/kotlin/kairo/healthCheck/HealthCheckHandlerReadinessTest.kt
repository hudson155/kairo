package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.dependencyInjection.getInstance
import kairo.restTesting.client
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckHandlerReadinessTest : HealthCheckHandlerTest() {
  @Test
  fun healthy(): Unit = runTest {
    client.request(HealthCheckApi.Readiness)
      .shouldBe(
        HealthCheckRep(
          status = HealthCheckRep.Status.Healthy,
          checks = mapOf(
            "server" to HealthCheckRep.Check(status = HealthCheckRep.Status.Healthy),
            "test" to HealthCheckRep.Check(status = HealthCheckRep.Status.Healthy),
          ),
        ),
      )
  }

  @Test
  fun unhealthy(): Unit = runTest {
    val healthCheckService = injector.getInstance<HealthCheckService>() as TestHealthCheckService
    healthCheckService.testStatus = HealthCheckRep.Status.Unhealthy

    client.request(HealthCheckApi.Readiness)
      .shouldBe(
        HealthCheckRep(
          status = HealthCheckRep.Status.Unhealthy,
          checks = mapOf(
            "server" to HealthCheckRep.Check(status = HealthCheckRep.Status.Healthy),
            "test" to HealthCheckRep.Check(status = HealthCheckRep.Status.Unhealthy),
          ),
        ),
      )
  }
}
