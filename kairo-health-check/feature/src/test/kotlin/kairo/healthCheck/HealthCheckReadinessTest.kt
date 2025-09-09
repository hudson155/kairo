package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.testing.setup
import kairo.testing.test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckReadinessTest {
  private lateinit var check: HealthCheck

  private val healthCheckService: HealthCheckService =
    HealthCheckService(
      healthChecks = buildMap {
        put("test", HealthCheck { check.check() })
      },
      timeout = 2.seconds,
    )

  @Test
  fun success(): Unit =
    runTest {
      setup {
        check = HealthCheck {}
      }
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
      setup {
        check = HealthCheck { error("Something went wrong!") }
      }
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
      setup {
        check = HealthCheck {
          delay(Duration.INFINITE)
        }
      }
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
