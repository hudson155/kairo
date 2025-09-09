package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.testing.setup
import kairo.testing.test
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckReadinessTest {
  private var check: Boolean = false

  private val healthCheckService: HealthCheckService =
    HealthCheckService(
      healthChecks = buildMap {
        put("test", HealthCheck { check(check) })
      },
      timeout = 2.seconds,
    )

  @Test
  fun success(): Unit =
    runTest {
      setup {
        check = true
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
