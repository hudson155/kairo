package kairo.googleAppEngine

import io.kotest.matchers.shouldBe
import kairo.dependencyInjection.getInstance
import kairo.healthCheck.HealthCheckRep
import kairo.healthCheck.HealthCheckService
import kairo.restTesting.client
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class GoogleAppEngineWarmupTest : GoogleAppEngineFeatureTest() {
  @Test
  fun healthy(): Unit = runTest {
    client.request(GoogleAppEngineApi.Warmup)
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

    client.request(GoogleAppEngineApi.Warmup)
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
