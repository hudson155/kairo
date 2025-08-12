package kairo.cyclicBarrier

import io.kotest.assertions.async.shouldTimeout
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
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
  fun `1 party, simple happy path`(): Unit =
    runTest {
      val barrier = CyclicBarrier(1)
      launch {
        barrier.await()
      }
    }

  @Test
  fun `2 parties, 1 await timeout`(): Unit =
    runTest {
      val barrier = CyclicBarrier(2)
      launch {
        shouldTimeout(1.seconds) {
          barrier.await()
        }
      }
    }

  @Test
  fun `2 parties, 3 awaits timeout`(): Unit =
    runTest {
      val barrier = CyclicBarrier(2)
      repeat(2) {
        launch {
          barrier.await()
        }
      }
      launch {
        shouldTimeout(1.seconds) {
          barrier.await()
        }
      }
    }

  @Test
  fun `1000 parties, simple happy path`(): Unit =
    runTest {
      val barrier = CyclicBarrier(1000)
      repeat(1000) {
        launch {
          barrier.await()
        }
      }
    }

  @Test
  fun `1000 parties, 999 awaits timeout`(): Unit =
    runTest {
      val barrier = CyclicBarrier(1000)
      repeat(999) {
        launch {
          shouldTimeout(1.seconds) {
            barrier.await()
          }
        }
      }
    }

  @Test
  fun `1000 parties, 1001 awaits timeout`(): Unit =
    runTest {
      val barrier = CyclicBarrier(1000)
      repeat(1000) {
        launch {
          barrier.await()
        }
      }
      launch {
        shouldTimeout(1.seconds) {
          barrier.await()
        }
      }
    }

  @Test
  fun `4 parties, cancellation`(): Unit =
    runTest {
      val barrier = CyclicBarrier(4)
      repeat(2) {
        launch {
          shouldThrow<CancellationException> {
            barrier.await()
          }
        }
      }
      val third = launch {
        shouldThrow<CancellationException> {
          barrier.await()
        }
      }
      delay(10.milliseconds)
      third.cancel()
    }

  @Test
  fun `4 parties, reuse after cancellation`(): Unit =
    runTest {
      val barrier = CyclicBarrier(4)
      repeat(2) {
        launch {
          barrier.await()
        }
      }
      val third = launch {
        barrier.await()
      }
      delay(10.milliseconds)
      third.cancel()
      repeat(4) {
        launch {
          barrier.await()
        }
      }
    }

  @Test
  fun `event trace, single iteration`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4)
      List(4) {
        launch {
          events.update { it + "before await" }
          barrier.await()
          events.update { it + "after await" }
        }
      }.joinAll()
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
      List(80) {
        launch {
          events.update { it + "before await" }
          barrier.await()
          events.update { it + "after await" }
        }
      }.joinAll()
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
          eventList = eventList.indexOfFirst { it == "after await" }.let { eventList.take(it) + eventList.drop(it + 1) }
        }
      }
      eventList.shouldBeEmpty()
    }

  @Test
  fun `event trace with barrier command, single iteration`(): Unit =
    runTest {
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(4) { events.update { it + "barrier command" } }
      List(4) {
        launch {
          events.update { it + "before await" }
          barrier.await()
          events.update { it + "after await" }
        }
      }.joinAll()
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
      List(80) {
        launch {
          events.update { it + "before await" }
          barrier.await()
          events.update { it + "after await" }
        }
      }.joinAll()
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
          eventList = eventList.indexOfFirst { it == "after await" }.let { eventList.take(it) + eventList.drop(it + 1) }
        }
      }
      eventList.shouldBeEmpty()
    }
}
