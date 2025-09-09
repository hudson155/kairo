package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.testing.test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckReadinessTest {
  private fun healthCheckService(check: suspend () -> Unit): HealthCheckService =
    HealthCheckService(
      healthChecks = buildMap {
        put("test", HealthCheck { check() })
      },
      timeout = 2.seconds,
    )

  @Test
  fun success(): Unit =
    runTest {
      val healthCheckService = healthCheckService {}
      test {
        healthCheckService.readiness()
          .shouldBe(
            HealthCheckRep(
              success = true,
              checks = mapOf("test" to true),
            ),
          )
      }
    }

  @Test
  fun failure(): Unit =
    runTest {
      val healthCheckService = healthCheckService { error("Something went wrong!") }
      test {
        healthCheckService.readiness()
          .shouldBe(
            HealthCheckRep(
              success = false,
              checks = mapOf("test" to false),
            ),
          )
      }
    }

  @Test
  fun timeout(): Unit =
    runTest {
      val healthCheckService = healthCheckService { delay(Duration.INFINITE) }
      test {
        healthCheckService.readiness()
          .shouldBe(
            HealthCheckRep(
              success = false,
              checks = mapOf("test" to false),
            ),
          )
      }
    }
}
