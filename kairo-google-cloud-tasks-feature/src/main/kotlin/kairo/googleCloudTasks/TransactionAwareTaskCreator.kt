package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint
import kotlin.coroutines.coroutineContext

public class TransactionAwareTaskCreator(
  private val delegate: TaskCreator,
) : TaskCreator() {
  public override suspend fun <E : RestEndpoint<I, *>, I : Any> create(endpoint: E) {
    val context = coroutineContext[TaskContext]
    if (context != null) {
      context.add { delegate.create(endpoint) }
    } else {
      delegate.create(endpoint)
    }
  }
}
