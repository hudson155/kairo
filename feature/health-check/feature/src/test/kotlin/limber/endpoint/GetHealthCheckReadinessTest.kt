package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.HealthCheckApi
import limber.rep.HealthCheckRep
import limber.service.HealthCheckService
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeServerError
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetHealthCheckReadinessTest : IntegrationTest() {
  @Test
  fun healthy() {
    test {
      val expected = HealthCheckRep(
        state = HealthCheckRep.State.Healthy,
        checks = mapOf(
          "http" to HealthCheckRep.Check("http", HealthCheckRep.State.Healthy),
          "server" to HealthCheckRep.Check("server", HealthCheckRep.State.Healthy),
        ),
      )
      healthCheckClient(HealthCheckApi.GetReadiness)
        .shouldBe(expected)
    }
  }

  @Test
  fun unhealthy() {
    testSetup("Set unhealthy") {
      injector.getInstance(HealthCheckService::class.java).server = HealthCheckRep.State.Unhealthy
    }

    test {
      val expected = HealthCheckRep(
        state = HealthCheckRep.State.Unhealthy,
        checks = mapOf(
          "http" to HealthCheckRep.Check("http", HealthCheckRep.State.Healthy),
          "server" to HealthCheckRep.Check("server", HealthCheckRep.State.Unhealthy),
        ),
      )
      shouldBeServerError(expected) {
        healthCheckClient(HealthCheckApi.GetReadiness)
      }
    }
  }
}
