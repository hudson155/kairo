package limber.feature.event

import com.google.inject.Inject
import limber.transaction.TransactionManager
import kotlin.coroutines.coroutineContext

/**
 * This event publisher supports [TransactionManager] transactions.
 * It uses the delegate pattern to allow flexibility for the underlying event publisher implementation.
 * Any calls to [publish] will be delayed until the transaction is committed.
 */
public class TransactionAwareEventPublisher<in T : Any>(
  private val delegate: EventPublisher<T>,
) : EventPublisher<T>() {
  public class Factory @Inject constructor(
    private val delegate: EventPublisher.Factory,
  ) : EventPublisher.Factory() {
    override fun <T : Any> invoke(topicName: String): EventPublisher<T> =
      TransactionAwareEventPublisher(delegate.invoke(topicName))

    override fun start() {
      delegate.start()
    }

    override fun stop() {
      delegate.stop()
    }
  }

  override suspend fun publish(type: EventType, body: T) {
    val context = checkNotNull(coroutineContext[EventContext])
    context.add {
      delegate.publish(type, body)
    }
  }
}
