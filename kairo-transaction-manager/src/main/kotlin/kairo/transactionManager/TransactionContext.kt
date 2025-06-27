package kairo.transactionManager

import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.currentCoroutineContext

/**
 * When inside a transaction, [TransactionContext] keeps track of what transaction types are active.
 * This allows nested transactions to avoid creating duplicate resources.
 * It also allows for proper cleanup of transaction resources.
 */
internal class TransactionContext : AbstractCoroutineContextElement(TransactionContext) {
  private val types: MutableMap<TransactionType, Unit> = ConcurrentHashMap()

  operator fun contains(type: TransactionType): Boolean =
    type in types

  operator fun plusAssign(type: TransactionType) {
    types += type to Unit
  }

  operator fun minusAssign(type: TransactionType) {
    types -= type
  }

  internal companion object : CoroutineContext.Key<TransactionContext>
}

internal suspend fun getTransactionContext(): TransactionContext? =
  currentCoroutineContext()[TransactionContext]
