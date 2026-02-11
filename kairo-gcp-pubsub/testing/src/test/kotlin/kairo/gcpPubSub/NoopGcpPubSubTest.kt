package kairo.gcpPubSub

import io.kotest.assertions.throwables.shouldThrow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class NoopGcpPubSubTest {
  private val gcpPubSub: GcpPubSub = NoopGcpPubSub()

  @Test
  fun `publish throws NotImplementedError`(): Unit =
    runTest {
      shouldThrow<NotImplementedError> {
        gcpPubSub.publish("test-topic", GcpPubSubMessage(data = "hello"))
      }
    }

  @Test
  fun `subscribe throws NotImplementedError`() {
    shouldThrow<NotImplementedError> {
      gcpPubSub.subscribe("test-sub") {}
    }
  }

  @Test
  fun `close does not throw`() {
    gcpPubSub.close()
  }
}
