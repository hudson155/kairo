package kairo.server

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
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

@Suppress("LongMethod")
internal class ServerStartTest {
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
      server.state.shouldBe(ServerState.Default)
      server.start()
      server.state.shouldBe(ServerState.Running)
    }

  @Test
  fun `Feature start exception (first one fails)`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      /**
       * The signal is used to ensure that "Test(0)" starts before "Test(1)".
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
                  try {
                    @Suppress("ThrowingExceptionsWithoutMessageOrCause", "TooGenericExceptionThrown")
                    throw RuntimeException("Exception from Test (1)")
                  } finally {
                    signal.complete(Unit)
                  }
                }
                stop { _ ->
                  events.update { it + "stop Test (0)" }
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
                  signal.await()
                  delay(1.seconds)
                  events.update { it + "start Test (1)" }
                }
                stop { _ ->
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
      server.state.shouldBe(ServerState.Default)
      shouldThrow<RuntimeException> {
        server.start()
      }.shouldHaveMessage("Exception from Test (1)")
      server.state.shouldBe(ServerState.Default)
      val eventList = events.value
      eventList[0].shouldBe("start Test (0)")
      eventList.subList(1, 3).shouldContainExactlyInAnyOrder(
        "stop Test (0)",
        "stop Test (1)",
      )
      eventList.shouldHaveSize(3)
    }

  @Test
  fun `Feature start exception (last one fails)`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      /**
       * The signal is used to ensure that "Test(0)" starts before "Test(1)".
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
                  signal.complete(Unit)
                }
                stop { _ ->
                  events.update { it + "stop Test (0)" }
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
                  signal.await()
                  events.update { it + "start Test (1)" }
                  @Suppress("ThrowingExceptionsWithoutMessageOrCause", "TooGenericExceptionThrown")
                  throw RuntimeException("Exception from Test (1)")
                }
                stop { _ ->
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
      server.state.shouldBe(ServerState.Default)
      shouldThrow<RuntimeException> {
        server.start()
      }.shouldHaveMessage("Exception from Test (1)")
      server.state.shouldBe(ServerState.Default)
      val eventList = events.value
      eventList[0].shouldBe("start Test (0)")
      eventList[1].shouldBe("start Test (1)")
      eventList.subList(2, 4).shouldContainExactlyInAnyOrder(
        "stop Test (0)",
        "stop Test (1)",
      )
      eventList.shouldHaveSize(4)
    }
}
