package kairo.googleCloudTasks

import kotlin.coroutines.coroutineContext

/**
 * Wraps an existing [TaskCreator] instance to make it aware of Kairo transactions.
 * See [TaskTransaction].
 */
public class TransactionAwareTaskCreator(
  private val delegate: TaskCreator,
) : TaskCreator() {
  override suspend fun create(task: Task) {
    val context = coroutineContext[TaskContext]
    if (context != null) {
      context.add { delegate.create(task) }
    } else {
      delegate.create(task)
    }
  }
}
