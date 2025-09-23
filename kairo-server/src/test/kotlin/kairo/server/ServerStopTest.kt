package kairo.server

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kairo.feature.Feature
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ServerStopTest {
  @Test
  fun `happy path`(): Unit =
    runTest {
      val features = listOf(
        object : Feature() {
          override val name: String = "Test (0)"
        },
        object : Feature() {
          override val name: String = "Test (1)"
        },
      )
      val server = Server(
        name = "Test",
        features = features,
      )
      server.start()
      server.state.shouldBe(ServerState.Running)
      server.stop()
      server.state.shouldBe(ServerState.Default)
    }

  @Test
  fun `Feature stop exception (first one fails)`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      /**
       * The signal is used to ensure that "Test(0)" stops before "Test(1)".
       */
      val signal = CompletableDeferred<Unit>()
      val features = listOf(
        object : Feature() {
          override val name: String = "Test (0)"

          override val lifecycle: List<LifecycleHandler> =
            lifecycle {
              handler {
                start { _ ->
                  events.update { it + "start Test (0)" }
                }
                stop { _ ->
                  events.update { it + "stop Test (0)" }
                  try {
                    @Suppress("ThrowingExceptionsWithoutMessageOrCause", "TooGenericExceptionThrown")
                    throw RuntimeException("Exception from Test (1)")
                  } finally {
                    signal.complete(Unit)
                  }
                }
              }
            }
        },
        object : Feature() {
          override val name: String = "Test (1)"

          override val lifecycle: List<LifecycleHandler> =
            lifecycle {
              handler {
                start { _ ->
                  events.update { it + "start Test (1)" }
                }
                stop { _ ->
                  signal.await()
                  delay(1.seconds)
                  events.update { it + "stop Test (1)" }
                }
              }
            }
        },
      )
      val server = Server(
        name = "Test",
        features = features,
      )
      server.start()
      server.state.shouldBe(ServerState.Running)
      shouldNotThrowAny {
        server.stop()
      }
      server.state.shouldBe(ServerState.Default)
      val eventList = events.value
      eventList.subList(0, 2).shouldContainExactlyInAnyOrder(
        "start Test (0)",
        "start Test (1)",
      )
      eventList[2].shouldBe("stop Test (0)")
      eventList[3].shouldBe("stop Test (1)")
      eventList.shouldHaveSize(4)
    }

  @Test
  fun `Feature stop exception (last one fails)`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      /**
       * The signal is used to ensure that "Test(0)" stops before "Test(1)".
       */
      val signal = CompletableDeferred<Unit>()
      val features = listOf(
        object : Feature() {
          override val name: String = "Test (0)"

          override val lifecycle: List<LifecycleHandler> =
            lifecycle {
              handler {
                start { _ ->
                  events.update { it + "start Test (0)" }
                }
                stop { _ ->
                  events.update { it + "stop Test (0)" }
                  signal.complete(Unit)
                }
              }
            }
        },
        object : Feature() {
          override val name: String = "Test (1)"

          override val lifecycle: List<LifecycleHandler> =
            lifecycle {
              handler {
                start { _ ->
                  events.update { it + "start Test (1)" }
                }
                stop { _ ->
                  signal.await()
                  events.update { it + "stop Test (1)" }
                  @Suppress("ThrowingExceptionsWithoutMessageOrCause", "TooGenericExceptionThrown")
                  throw RuntimeException("Exception from Test (1)")
                }
              }
            }
        },
      )
      val server = Server(
        name = "Test",
        features = features,
      )
      server.start()
      server.state.shouldBe(ServerState.Running)
      shouldNotThrowAny {
        server.stop()
      }
      server.state.shouldBe(ServerState.Default)
      val eventList = events.value
      eventList.subList(0, 2).shouldContainExactlyInAnyOrder(
        "start Test (0)",
        "start Test (1)",
      )
      eventList[2].shouldBe("stop Test (0)")
      eventList[3].shouldBe("stop Test (1)")
      eventList.shouldHaveSize(4)
    }
}
