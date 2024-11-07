package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.restTesting.client
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckLivenessTest : HealthCheckFeatureTest() {
  @Test
  fun test(): Unit = runTest {
    client.request(HealthCheckApi.Liveness).shouldBe(Unit)
  }
}
