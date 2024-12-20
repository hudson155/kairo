package kairo.googleCloudTasks

import com.google.inject.Inject
import kairo.transactionManager.TransactionManager
import kairo.transactionManager.TransactionType
import kairo.util.kairoEquals
import kairo.util.kairoHashCode
import kairo.util.kairoToString
import kotlin.coroutines.CoroutineContext

/**
 * When creating tasks within a transaction, they will not be published until the commit phase.
 *
 * Note: [TaskTransaction]s should typically be outside of SQL transactions
 * so that they commit only after the SQL transaction commits.
 *
 * WARNING: Although GCP's SLA is very good, it is still possible for the [TaskTransaction] commit phase to fail.
 * One of the most common cases when this would happen is if the task queue does not exist.
 * Take precautions to prevent that case.
 * If the commit phase does fail, data corruption is likely.
 * In this case, [TransactionManager] will log an error.
 */
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

  override fun equals(other: Any?): Boolean =
    kairoEquals(
      other = other,
      properties = emptyList()
    )

  override fun hashCode(): Int =
    kairoHashCode()

  override fun toString(): String =
    kairoToString(
      name = TaskTransaction::class.simpleName!!,
      properties = emptyList(),
    )
}
