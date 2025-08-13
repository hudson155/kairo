package kairo.cyclicBarrier

import io.kotest.assertions.async.shouldTimeout
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Test

internal class CyclicBarrierTest {
  @Test
  fun `non-positive parties`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        CyclicBarrier(0)
      }
      shouldThrow<IllegalArgumentException> {
        CyclicBarrier(-1)
      }
    }

  @Test
  fun `happy path (1 party)`(): Unit =
    runTest {
      val barrier = CyclicBarrier(1)
      supervisorScope {
        repeat(2) {
          shouldNotThrowAny {
            barrier.await()
          }
        }
      }
    }

  @Test
  fun `happy path (2 parties)`(): Unit =
    runTest {
      val barrier = CyclicBarrier(2)
      supervisorScope {
        repeat(2) {
          launch {
            shouldNotThrowAny {
              barrier.await()
            }
          }
        }
      }
    }

  @Test
  fun `await timeout`(): Unit =
    runTest {
      val barrier = CyclicBarrier(2)
      supervisorScope {
        launch {
          shouldTimeout(1.seconds) {
            barrier.await()
          }
        }
      }
    }

  @Test
  fun `10,000 parties`(): Unit =
    runTest {
      val barrier = CyclicBarrier(10_000)
      supervisorScope {
        withTimeout(1.seconds) {
          List(10_000) {
            launch {
              barrier.await()
            }
          }.joinAll()
        }
      }
    }

  @Test
  fun `10,000 cycles`(): Unit =
    runTest {
      val barrier = CyclicBarrier(2)
      supervisorScope {
        withTimeout(1.seconds) {
          List(20_000) {
            launch {
              barrier.await()
            }
          }.joinAll()
        }
      }
    }

  @Test
  fun cancellation(): Unit =
    runTest {
      val barrier = CyclicBarrier(3)
      supervisorScope {
        launch {
          shouldThrow<CancellationException> {
            barrier.await()
          }.shouldHaveMessage("manually cancelled")
        }
        val second = launch {
          shouldThrow<CancellationException> {
            barrier.await()
          }.shouldHaveMessage("manually cancelled")
        }
        delay(10.milliseconds)
        second.cancel("manually cancelled")
        repeat(3) {
          launch {
            shouldNotThrowAny {
              barrier.await()
            }
          }
        }
      }
    }

  @Test
  fun `barrier cancellation`(): Unit =
    runTest {
      val cancel = generateSequence(true) { false }.iterator()
      val barrier = CyclicBarrier(3) {
        if (cancel.next()) {
          throw CancellationException("manually cancelled")
        }
      }
      supervisorScope {
        repeat(3) {
          launch {
            shouldThrow<CancellationException> {
              barrier.await()
            }.shouldHaveMessage("manually cancelled")
          }
        }
        repeat(3) {
          launch {
            shouldNotThrowAny {
              barrier.await()
            }
          }
        }
      }
    }

  @Test
  fun `barrier exception`(): Unit =
    runTest {
      val cancel = generateSequence(true) { false }.iterator()
      val barrier = CyclicBarrier(3) {
        if (cancel.next()) {
          @Suppress("ThrowingExceptionsWithoutMessageOrCause", "TooGenericExceptionThrown")
          throw RuntimeException()
        }
      }
      supervisorScope {
        repeat(3) {
          launch {
            shouldThrow<CyclicBarrierException> {
              barrier.await()
            }.shouldHaveCauseInstanceOf<RuntimeException>()
          }
        }
        repeat(3) {
          launch {
            shouldNotThrowAny {
              barrier.await()
            }
          }
        }
      }
    }

  @Test
  fun `event trace, single iteration`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4)
      supervisorScope {
        List(4) {
          launch {
            events.update { it + "before await" }
            barrier.await()
            events.update { it + "after await" }
          }
        }.joinAll()
      }
      events.value.shouldContainExactly(
        "before await",
        "before await",
        "before await",
        "before await",
        "after await",
        "after await",
        "after await",
        "after await",
      )
    }

  @Test
  fun `event trace, multiple iterations`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4)
      supervisorScope {
        List(80) {
          launch {
            events.update { it + "before await" }
            barrier.await()
            events.update { it + "after await" }
          }
        }.joinAll()
      }
      var eventList = events.value
      repeat(20) {
        eventList.take(4).shouldContainExactly(
          "before await",
          "before await",
          "before await",
          "before await",
        )
        eventList = eventList.drop(4)
        repeat(4) {
          eventList = eventList
            .indexOfFirst { event -> event == "after await" }
            .let { i -> eventList.take(i) + eventList.drop(i + 1) }
        }
      }
      eventList.shouldBeEmpty()
    }

  @Test
  fun `event trace with barrier command, single iteration`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4) { events.update { it + "barrier command" } }
      supervisorScope {
        List(4) {
          backgroundScope.launch {
            events.update { it + "before await" }
            barrier.await()
            events.update { it + "after await" }
          }
        }.joinAll()
      }
      events.value.shouldContainExactly(
        "before await",
        "before await",
        "before await",
        "before await",
        "barrier command",
        "after await",
        "after await",
        "after await",
        "after await",
      )
    }

  @Test
  fun `event trace with barrier command, multiple iterations`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4) { events.update { it + "barrier command" } }
      supervisorScope {
        List(80) {
          backgroundScope.launch {
            events.update { it + "before await" }
            barrier.await()
            events.update { it + "after await" }
          }
        }.joinAll()
      }
      var eventList = events.value
      repeat(20) {
        eventList.take(5).shouldContainExactly(
          "before await",
          "before await",
          "before await",
          "before await",
          "barrier command",
        )
        eventList = eventList.drop(5)
        repeat(4) {
          eventList =
            eventList
              .indexOfFirst { event -> event == "after await" }
              .let { i -> eventList.take(i) + eventList.drop(i + 1) }
        }
      }
      eventList.shouldBeEmpty()
    }
}
