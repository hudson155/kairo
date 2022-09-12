package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.HealthCheckApi
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import org.junit.jupiter.api.Test

internal class GetHealthCheckLivenessTest : IntegrationTest() {
  @Test
  fun healthy() {
    integrationTest {
      healthCheckClient(HealthCheckApi.GetLiveness)
        .shouldBe(Unit)
    }
  }
}
