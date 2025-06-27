package kairo.googleCloudTasks

import java.util.LinkedList
import java.util.Queue
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.currentCoroutineContext

internal class TaskContext : AbstractCoroutineContextElement(TaskContext) {
  val blocks: Queue<suspend () -> Unit> = LinkedList()

  fun add(block: suspend () -> Unit) {
    blocks.add(block)
  }

  internal companion object : CoroutineContext.Key<TaskContext>
}

internal suspend fun getTaskContext(): TaskContext? =
  currentCoroutineContext()[TaskContext]
