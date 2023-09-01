package limber.feature.event

import java.util.LinkedList
import java.util.Queue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class EventContext : AbstractCoroutineContextElement(Key) {
  val blocks: Queue<suspend () -> Unit> = LinkedList()

  fun add(block: suspend () -> Unit) {
    blocks.add(block)
  }

  internal companion object Key : CoroutineContext.Key<EventContext>
}

internal suspend fun getEventContext(): EventContext? =
  coroutineContext[EventContext]
