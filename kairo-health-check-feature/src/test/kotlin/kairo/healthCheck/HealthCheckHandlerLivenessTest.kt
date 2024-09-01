package kairo.healthCheck

import io.kotest.matchers.shouldBe
import kairo.restTesting.client
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class HealthCheckHandlerLivenessTest : HealthCheckHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    client.request(HealthCheckApi.Liveness).shouldBe(Unit)
  }
}
