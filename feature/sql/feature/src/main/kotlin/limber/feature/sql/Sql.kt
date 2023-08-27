package limber.feature.sql

import com.google.inject.Inject
import kotlinx.coroutines.withContext
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

public class Sql @Inject constructor(
  private val jdbi: Jdbi,
) {
  internal class Context(
    internal val handle: Handle,
  ) : AbstractCoroutineContextElement(Key) {
    internal companion object Key : CoroutineContext.Key<Context>
  }

  @Suppress("MemberNameEqualsClassName")
  public suspend fun <T> sql(block: suspend (handle: Handle) -> T): T {
    val context = coroutineContext[Context]

    /**
     * If there's already a SQL context, we can just run the block directly.
     * This happens when calls to [sql] are nested.
     */
    if (context != null) {
      return run(block)
    }

    /**
     * Otherwise, we should open a JDBI [Handle] and begin a transaction,
     * being sure to clean up both afterward.
     * We also need to create a context before running the block.
     */
    return jdbi.open().use { handle ->
      handle.begin()
      @Suppress("TooGenericExceptionCaught")
      try {
        val result = withContext(Context(handle)) {
          return@withContext run(block)
        }
        handle.commit()
        return@use result
      } catch (e: Exception) {
        handle.rollback()
        throw e
      }
    }
  }

  private suspend fun <T> run(block: suspend (handle: Handle) -> T): T {
    val handle = checkNotNull(coroutineContext[Context]).handle
    return block(handle)
  }
}
