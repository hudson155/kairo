package kairo.googleCloudTasks

import java.util.LinkedList
import java.util.Queue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class TaskContext : AbstractCoroutineContextElement(key) {
  val blocks: Queue<suspend () -> Unit> = LinkedList()

  fun add(block: suspend () -> Unit) {
    blocks.add(block)
  }

  internal companion object {
    internal val key: CoroutineContext.Key<TaskContext> =
      object : CoroutineContext.Key<TaskContext> {}
  }
}

internal suspend fun getTaskContext(): TaskContext? =
  coroutineContext[TaskContext.key]
