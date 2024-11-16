package kairo.googleCloudTasks

import kotlin.coroutines.coroutineContext

/**
 * Wraps an existing [GoogleCloudTasks] instance to make it aware of Kairo transactions.
 * See [TaskTransaction].
 */
public class TransactionAwareGoogleCloudTasks(
  private val delegate: GoogleCloudTasks,
) : GoogleCloudTasks() {
  override suspend fun create(task: Task) {
    val context = coroutineContext[TaskContext]
    if (context != null) {
      context.add { delegate.create(task) }
    } else {
      delegate.create(task)
    }
  }

  override fun close() {
    delegate.close()
  }
}
