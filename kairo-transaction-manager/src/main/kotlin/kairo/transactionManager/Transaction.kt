package kairo.transactionManager

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Stack
import kairo.logging.ifDebugEnabled
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.withContext

private val logger: KLogger = KotlinLogging.logger {}

/**
 * See [TransactionManager].
 */
internal class Transaction(
  private val types: List<TransactionType>,
) {
  private val contexts: Stack<Pair<TransactionType.WithContext, CoroutineContext>> = Stack()
  private val openTransactions: Stack<TransactionType> = Stack()

  suspend fun <T> transaction(block: suspend () -> T): T {
    try {
      val context = createContext()
      return withContext(context) {
        val transactionContext = checkNotNull(getTransactionContext())
        try {
          beginAll(transactionContext)
          val result = runBlock(block)
          commitAll(transactionContext)
          return@withContext result
        } finally {
          rollbackAll(transactionContext)
        }
      }
    } finally {
      closeContexts()
    }
  }

  /**
   * Creates the [CoroutineContext] that will be used for this transaction.
   *
   * All transactions must occur within a [TransactionContext],
   * but these cannot be nested.
   * Therefore, a new [TransactionContext] is only created if one doesn't exist yet.
   *
   * The [CoroutineContext] will also include relevant contexts from event types.
   */
  private suspend fun createContext(): CoroutineContext {
    logger.debug { "Creating the context for a transaction.." }

    /**
     * Start off with an empty context.
     */
    var combinedContext: CoroutineContext = EmptyCoroutineContext

    /**
     * Create a new [TransactionContext] if one doesn't exist yet.
     */
    if (getTransactionContext() == null) {
      combinedContext += TransactionContext()
    }

    /**
     * Add relevant contexts from any types.
     */
    types.forEach { type ->
      if (type !is TransactionType.WithContext) {
        logger.debug { "Transaction type ${type::class.simpleName!!} has no context." }
        return@forEach
      }
      logger.debug { "Creating context for transaction type ${type::class.simpleName!!}." }
      try {
        val context = type.createContext()
        combinedContext += context
        contexts.push(Pair(type, context))
      } catch (e: Exception) {
        logger.warn(e) {
          "An error occurred while creating the context for a ${type::class.simpleName!!} transaction." +
            " Please make every effort to avoid this."
        }
        throw TransactionContextCreationFailedException(e)
      }
    }

    return combinedContext
  }

  private suspend fun beginAll(context: TransactionContext) {
    types.forEach { type ->
      logger.debug { "Beginning transaction type ${type::class.simpleName!!}." }
      try {
        type.begin()
        openTransactions.push(type)
      } catch (e: Exception) {
        logger.warn(e) {
          "An error occurred while beginning a ${type::class.simpleName!!} transaction." +
            " Please make every effort to avoid this."
        }
        throw TransactionBeginFailedException(e)
      }
      context += type
    }
  }

  private suspend fun <T> runBlock(block: suspend () -> T): T {
    try {
      return block()
    } catch (e: Exception) {
      logger.warn(logger.ifDebugEnabled(e)) { "An exception was thrown during a transaction. Rolling back." }
      throw e
    }
  }

  private suspend fun commitAll(context: TransactionContext) {
    while (openTransactions.isNotEmpty()) {
      val type = openTransactions.pop()
      logger.debug { "Committing transaction type ${type::class.simpleName!!}." }
      try {
        type.commit()
      } catch (e: Exception) {
        openTransactions.push(type)
        logger.error {
          "An error occurred while committing a ${type::class.simpleName!!} transaction." +
            " Please make every effort to avoid this." +
            " Data corruption is likely. Please investigate."
        }
        throw TransactionCommitFailedException(e)
      }
      context -= type
    }
  }

  private suspend fun rollbackAll(context: TransactionContext) {
    while (openTransactions.isNotEmpty()) {
      val type = openTransactions.pop()
      logger.debug { "Rolling back transaction type ${type::class.simpleName!!}." }
      try {
        type.rollback()
      } catch (e: Exception) {
        logger.error(e) {
          "An error occurred while rolling back a ${type::class.simpleName!!} transaction." +
            " Please make every effort to avoid this." +
            " Data corruption is possible. Please investigate."
        }
      }
      context -= type
    }
  }

  private suspend fun closeContexts() {
    while (contexts.isNotEmpty()) {
      val (type, context) = contexts.pop()
      logger.debug { "Closing context for transaction type ${type::class.simpleName!!}." }
      try {
        type.closeContext(context)
      } catch (e: Exception) {
        logger.error(e) {
          "An error occurred while rolling back a ${type::class.simpleName!!} transaction." +
            " Please make every effort to avoid this."
        }
      }
    }
  }
}
