package kairo.transactionManager

import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * When inside a transaction, [TransactionContext] keeps track of what transaction types are active.
 * This allows nested transactions to avoid creating duplicate resources.
 * It also allows for proper cleanup of transaction resources.
 */
internal class TransactionContext : AbstractCoroutineContextElement(ContextKey) {
  private val types: MutableMap<TransactionType, Unit> = ConcurrentHashMap()

  operator fun contains(type: TransactionType): Boolean =
    type in types

  operator fun plusAssign(type: TransactionType) {
    types += type to Unit
  }

  operator fun minusAssign(type: TransactionType) {
    types -= type
  }

  internal companion object ContextKey : CoroutineContext.Key<TransactionContext>
}

internal suspend fun getTransactionContext(): TransactionContext? =
  coroutineContext[TransactionContext]
