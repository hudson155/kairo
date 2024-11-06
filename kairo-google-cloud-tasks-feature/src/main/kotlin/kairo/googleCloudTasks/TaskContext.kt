package kairo.googleCloudTasks

import java.util.LinkedList
import java.util.Queue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class TaskContext : AbstractCoroutineContextElement(Key) {
  internal companion object Key : CoroutineContext.Key<TaskContext>

  val blocks: Queue<suspend () -> Unit> = LinkedList()

  fun add(block: suspend () -> Unit) {
    blocks.add(block)
  }
}

internal suspend fun getTaskContext(): TaskContext? =
  coroutineContext[TaskContext]
