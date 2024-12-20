package kairo.transactionManager

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

/**
 * Each distinct type of transaction (SQL transaction, event transaction, etc.)
 * will have exactly 1 implementation of [TransactionType].
 *
 * If the transaction needs to support [CoroutineContext]s, implement [TransactionType.WithContext] too.
 */
public abstract class TransactionType {
  public interface WithContext {
    /**
     * The implementation should create the relevant context.
     * It should not mount it (that is, it should not call [withContext]).
     */
    public suspend fun createContext(): CoroutineContext

    /**
     * The implementation should clean up resources within the coroutine context.
     * This will be a no-op in some instances.
     */
    public suspend fun closeContext(
      /**
       * The context is passed in because it's already unmounted by the time it needs to be closed.
       */
      context: CoroutineContext,
    )
  }

  public abstract suspend fun begin()

  public abstract suspend fun commit()

  public abstract suspend fun rollback()
}
