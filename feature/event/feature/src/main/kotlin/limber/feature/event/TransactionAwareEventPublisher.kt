package limber.feature.event

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
  public class Factory(
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
    /**
     * If we're in a transaction, use it.
     */
    coroutineContext[EventContext]?.add {
      delegate.publish(type, body)
    }

    /**
     * Otherwise, we can call the delegate directly.
     */
    delegate.publish(type, body)
  }
}
