package kairo.server

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.feature.Feature
import kairo.feature.LifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ServerStartTest {
  @Test
  fun `exception outside launch`(): Unit =
    runTest {
      var attempted1 = false
      val features = listOf(
        object : Feature() {
          override val name: String = "Test (0)"
          override fun CoroutineScope.on(event: LifecycleEvent): Nothing? {
            if (event == LifecycleEvent.Start) error("Fake failure!")
            return null
          }
        },
        object : Feature() {
          override val name: String = "Test (1)"
          override fun CoroutineScope.on(event: LifecycleEvent): Nothing? {
            attempted1 = true
            return null // TODO: Assert these but use events instead?
          }
        },
      )
      val server = Server(features)
      server.running.shouldBeFalse()
      shouldThrow<IllegalStateException> {
        server.start()
      }.shouldHaveMessage("Fake failure!")
      server.running.shouldBeFalse()
      shouldNotThrowAny {
        server.stop()
      }
      server.running.shouldBeFalse()
    }

  @Test
  fun `exception inside launch`(): Unit =
    runTest {
      val features = listOf(
        object : Feature() {
          override val name: String = "Test"
          override fun CoroutineScope.on(event: LifecycleEvent): Job =
            launch { if (event == LifecycleEvent.Start) error("Fake failure!") }
        },
      )
      val server = Server(features)
      server.running.shouldBeFalse()
      shouldThrow<IllegalStateException> {
        server.start()
      }.shouldHaveMessage("Fake failure!")
      server.running.shouldBeFalse()
      shouldNotThrowAny {
        server.stop()
      }
      server.running.shouldBeFalse()
    }
}
