package limber.transaction

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

/**
 * When inside a transaction, [TransactionContext] keeps track of what transaction types are active.
 * This allows nested transactions to avoid creating duplicate resources.
 * It also allows for proper cleanup of transaction resources.
 */
internal class TransactionContext : AbstractCoroutineContextElement(Key) {
  private val types: MutableSet<KClass<out TransactionType>> = mutableSetOf()

  operator fun contains(type: KClass<out TransactionType>): Boolean = type in types

  operator fun plusAssign(type: KClass<out TransactionType>) {
    types += type
  }

  operator fun minusAssign(type: KClass<out TransactionType>) {
    types -= type
  }

  internal companion object Key : CoroutineContext.Key<TransactionContext>
}

internal suspend fun getTransactionContext(): TransactionContext? =
  coroutineContext[TransactionContext]
