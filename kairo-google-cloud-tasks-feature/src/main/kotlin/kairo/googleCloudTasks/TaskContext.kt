package kairo.googleCloudTasks

import java.util.LinkedList
import java.util.Queue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class TaskContext : AbstractCoroutineContextElement(ContextKey) {
  val blocks: Queue<suspend () -> Unit> = LinkedList()

  fun add(block: suspend () -> Unit) {
    blocks.add(block)
  }

  internal companion object ContextKey : CoroutineContext.Key<TaskContext>
}

internal suspend fun getTaskContext(): TaskContext? =
  coroutineContext[TaskContext]
