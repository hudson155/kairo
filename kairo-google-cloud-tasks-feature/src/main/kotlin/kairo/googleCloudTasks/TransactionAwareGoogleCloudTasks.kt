package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint
import kotlin.coroutines.coroutineContext

/**
 * Wraps an existing [GoogleCloudTasks] instance to make it aware of Kairo transactions.
 * See [TaskTransaction].
 */
public class TransactionAwareGoogleCloudTasks(
  private val delegate: GoogleCloudTasks,
) : GoogleCloudTasks() {
  override suspend fun create(endpoint: RestEndpoint<*, *>) {
    val context = coroutineContext[TaskContext]
    if (context != null) {
      context.add { delegate.create(endpoint) }
    } else {
      delegate.create(endpoint)
    }
  }

  override fun close() {
    delegate.close()
  }
}
