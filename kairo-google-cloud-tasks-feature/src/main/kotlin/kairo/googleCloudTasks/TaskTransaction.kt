package kairo.googleCloudTasks

import com.google.inject.Inject
import kairo.transactionManager.TransactionType
import kotlin.coroutines.CoroutineContext

public class TaskTransaction @Inject constructor() : TransactionType(), TransactionType.WithContext {
  override suspend fun createContext(): CoroutineContext =
    TaskContext()

  override suspend fun begin(): Unit = Unit

  override suspend fun commit() {
    val context = checkNotNull(getTaskContext())
    val blocks = context.blocks
    while (blocks.isNotEmpty()) {
      blocks.remove().invoke()
    }
  }

  override suspend fun rollback() {
    val context = checkNotNull(getTaskContext())
    context.blocks.clear()
  }

  override suspend fun closeContext(context: CoroutineContext): Unit = Unit
}
