package kairo.server

import arrow.fx.coroutines.CyclicBarrier
import io.kotest.matchers.collections.shouldContainExactly
import kairo.feature.Feature
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Starts a server with 1000 parallel Features,
 * ensuring that everything happens in the right order.
 */
internal class ServerConcurrencyTest {
  @Test
  fun test(): Unit =
    runTest {
      val concurrency = 1000
      val events = MutableStateFlow(emptyList<String>())
      val barrier = CyclicBarrier(concurrency)
      val features = List(concurrency) { i ->
        object : Feature() {
          override val name: String = "Test ($i)"

          override val lifecycle: List<LifecycleHandler> =
            lifecycle {
              handler {
                start { _ ->
                  events.update { it + "start first" }
                  barrier.await()
                  events.update { it + "start second" }
                  barrier.await()
                }
                stop { _ ->
                  events.update { it + "stop first" }
                  barrier.await()
                  events.update { it + "stop second" }
                  barrier.await()
                }
              }
            }
        }
      }
      val server = Server(
        name = "Test",
        features = features,
      )
      events.update { it + "server created" }
      server.start()
      events.update { it + "server started" }
      server.stop()
      events.update { it + "server stopped" }
      val eventList = events.value
      eventList.shouldContainExactly(
        buildList {
          add("server created")
          addAll(List(concurrency) { "start first" })
          addAll(List(concurrency) { "start second" })
          add("server started")
          addAll(List(concurrency) { "stop first" })
          addAll(List(concurrency) { "stop second" })
          add("server stopped")
        },
      )
    }
}
