package kairo.server

import arrow.fx.coroutines.CyclicBarrier
import io.kotest.matchers.collections.shouldContainExactly
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
  internal sealed class Event {
    data class Basic(
      val description: String,
    ) : Event()

    data class Lifecycle(
      val event: LifecycleEvent,
      val description: String,
    ) : Event()
  }

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
            events.update { it + Event.Lifecycle(event, "first") }
            barrier.await()
            events.update { it + Event.Lifecycle(event, "second") }
            barrier.await()
          }
        }
      }
      val server = Server(
        name = "Test",
        features = features,
      )
      events.update { it + Event.Basic("server created") }
      server.start()
      events.update { it + Event.Basic("server started") }
      server.stop()
      events.update { it + Event.Basic("server stopped") }
      val eventList = events.value
      eventList.shouldContainExactly(
        buildList {
          add(Event.Basic("server created"))
          addAll(List(concurrency) { Event.Lifecycle(LifecycleEvent.Start, "first") })
          addAll(List(concurrency) { Event.Lifecycle(LifecycleEvent.Start, "second") })
          add(Event.Basic("server started"))
          addAll(List(concurrency) { Event.Lifecycle(LifecycleEvent.Stop, "first") })
          addAll(List(concurrency) { Event.Lifecycle(LifecycleEvent.Stop, "second") })
          add(Event.Basic("server stopped"))
        },
      )
    }
}
