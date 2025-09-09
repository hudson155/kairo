package kairo.healthCheck

import io.kotest.assertions.throwables.shouldNotThrowAny
import kairo.testing.test
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckLivenessTest {
  private val healthCheckService: HealthCheckService =
    HealthCheckService(
      healthChecks = buildMap {
        put("test", HealthCheck { error("Something went wrong!") })
      },
      timeout = 2.seconds,
    )

  @Test
  fun test(): Unit =
    runTest {
      test {
        shouldNotThrowAny {
          healthCheckService.liveness()
        }
      }
    }
}
