package kairo.transactionManager

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Key
import kotlin.coroutines.coroutineContext

/**
 * A transaction is a wrapper for some code that should be either committed or rolled back
 * depending on whether it completes successfully or throws an exception.
 *
 * [TransactionManager] supports arbitrary transaction types.
 * Some examples include SQL transactions and event transactions.
 *
 * This class has the following properties:
 *
 *   - Transactionality: If the operation completes successfully, the transaction will be committed.
 *     If the operation fails (throws an exception), the transaction will be rolled back.
 *     There is no way to roll the transaction back without throwing an exception.
 *   - Error handling: Errors during the transaction "begin", "commit", and "rollback" phases
 *     should be avoided by the underlying implementations.
 *     Of course, it's not possible to guarantee a perfect success rate.
 *       - If an error occurs during the "begin" phase,
 *         outer transactions (if any) will be rolled back.
 *       - If an error occurs during the "commit" phase,
 *         outer transactions will be rolled back instead of committed.
 *         Inner transactions were already committed, and can no longer be rolled back.
 *         Data corruption is likely.
 *       - If an error occurs during the "rollback" phase,
 *         outer transactions will continue to be rolled back.
 *         Inner transactions were already rolled back.
 *         Data corruption is possible.
 *   - Nested transactions: Nested transactions of the same type reuse the outer transaction.
 *     In the case of nested transactions of the same type,
 *     it's as if the inner transaction didn't even exist, and was a direct operation call instead.
 */
public class TransactionManager @Inject constructor(
  private val injector: Injector,
) {
  /**
   * Creates new transactions of type [types] (if they aren't already ongoing),
   * and runs the given [block].
   */
  public suspend fun <T> transaction(
    vararg types: Key<out TransactionType>,
    block: suspend () -> T,
  ): T =
    transaction(types.map { injector.getInstance(it) }, block)

  public suspend fun <T> transaction(
    types: List<TransactionType>,
    block: suspend () -> T,
  ): T {
    require(types.isNotEmpty()) { "Please specify at least 1 transaction type." }
    require(types.distinct() == types) { "Duplicate transaction types specified." }
    val context = coroutineContext[TransactionContext]
    val transaction =
      Transaction(types.filter { context == null || it !in context })
    return transaction.transaction(block)
  }
}
