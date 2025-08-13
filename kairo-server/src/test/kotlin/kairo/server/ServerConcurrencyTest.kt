package kairo.server

import io.kotest.matchers.collections.shouldContainExactly
import kairo.cyclicBarrier.CyclicBarrier
import kairo.feature.Feature
import kairo.feature.LifecycleEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Starts a server with 1000 parallel Features,
 * ensuring that everything happens in the right order.
 */
internal class ServerConcurrencyTest {
  internal data class Event(
    val event: LifecycleEvent,
    val phase: String,
  )

  @Test
  fun test(): Unit =
    runTest {
      val concurrency = 1000
      val events = MutableStateFlow(emptyList<Event>())
      val barrier = CyclicBarrier(concurrency)
      val features = List(concurrency) { i ->
        object : Feature() {
          override val name: String = "Test ($i)"
          override suspend fun on(event: LifecycleEvent) {
            events.update { it + Event(event, "first") }
            barrier.await()
            events.update { it + Event(event, "second") }
            barrier.await()
          }
        }
      }
      val server = Server(
        name = "Test",
        features = features,
      )
      server.start()
      server.stop()
      val eventList = events.value
      eventList.shouldContainExactly(
        buildList {
          addAll(List(concurrency) { Event(LifecycleEvent.Start, "first") })
          addAll(List(concurrency) { Event(LifecycleEvent.Start, "second") })
          addAll(List(concurrency) { Event(LifecycleEvent.Stop, "first") })
          addAll(List(concurrency) { Event(LifecycleEvent.Stop, "second") })
        },
      )
    }
}
