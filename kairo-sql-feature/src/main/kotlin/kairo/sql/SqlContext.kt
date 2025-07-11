package kairo.sql

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.currentCoroutineContext
import org.jdbi.v3.core.Handle

public class SqlContext(
  internal val handle: Handle,
) : AbstractCoroutineContextElement(SqlContext) {
  public companion object : CoroutineContext.Key<SqlContext>
}

internal suspend fun getSqlContext(): SqlContext? =
  currentCoroutineContext()[SqlContext]
