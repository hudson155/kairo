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
  private class Content {
    val notes: MutableList<String> = mutableListOf()
  }

  private val types: MutableMap<KClass<out TransactionType>, Content> = mutableMapOf()

  operator fun contains(type: KClass<out TransactionType>): Boolean = type in types

  operator fun plusAssign(type: KClass<out TransactionType>) {
    types += type to Content()
  }

  operator fun minusAssign(type: KClass<out TransactionType>) {
    types -= type
  }

  fun addNote(note: String) {
    types.values.forEach { it.notes += note }
  }

  fun getNotes(type: KClass<out TransactionType>): List<String> =
    checkNotNull(types[type]).notes

  internal companion object Key : CoroutineContext.Key<TransactionContext>
}

internal suspend fun getTransactionContext(): TransactionContext? =
  coroutineContext[TransactionContext]
