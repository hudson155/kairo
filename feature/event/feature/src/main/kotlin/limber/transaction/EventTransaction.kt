package limber.transaction

import com.google.inject.Inject
import limber.feature.event.EventContext
import limber.feature.event.getEventContext
import kotlin.coroutines.CoroutineContext

public class EventTransaction @Inject constructor() : TransactionType(), TransactionType.WithContext {
  override suspend fun createContext(): CoroutineContext =
    EventContext()

  override suspend fun begin(): Unit = Unit

  override suspend fun commit() {
    val context = checkNotNull(getEventContext())
    val blocks = context.blocks
    while (blocks.isNotEmpty()) {
      blocks.remove().invoke()
    }
  }

  override suspend fun rollback() {
    val context = checkNotNull(getEventContext())
    context.blocks.clear()
  }

  override suspend fun closeContext(context: CoroutineContext): Unit = Unit
}
