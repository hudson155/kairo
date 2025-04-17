package kairo.sql

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import org.jdbi.v3.core.Handle

public class SqlContext(
  internal val handle: Handle,
) : AbstractCoroutineContextElement(key) {
  public companion object {
    internal val key: CoroutineContext.Key<SqlContext> =
      object : CoroutineContext.Key<SqlContext> {}
  }
}

internal suspend fun getSqlContext(): SqlContext? =
  coroutineContext[SqlContext.key]
