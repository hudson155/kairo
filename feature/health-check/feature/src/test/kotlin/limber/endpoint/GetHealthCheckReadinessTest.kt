package limber.endpoint

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.ServerResponseException
import limber.api.HealthCheckApi
import limber.rep.HealthCheckRep
import limber.service.HealthCheckService
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import org.junit.jupiter.api.Test

internal class GetHealthCheckReadinessTest : IntegrationTest() {
  @Test
  fun healthy() {
    integrationTest {
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
    integrationTest {
      injector.getInstance(HealthCheckService::class.java).server = HealthCheckRep.State.Unhealthy
      val expected = HealthCheckRep(
        state = HealthCheckRep.State.Unhealthy,
        checks = mapOf(
          "http" to HealthCheckRep.Check("http", HealthCheckRep.State.Healthy),
          "server" to HealthCheckRep.Check("server", HealthCheckRep.State.Unhealthy),
        ),
      )
      val e = shouldThrow<ServerResponseException> {
        healthCheckClient(HealthCheckApi.GetReadiness)
      }
      e.response.body<HealthCheckRep>().shouldBe(expected)
    }
  }
}
