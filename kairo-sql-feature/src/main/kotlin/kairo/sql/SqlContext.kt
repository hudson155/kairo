package kairo.sql

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import org.jdbi.v3.core.Handle

public class SqlContext(
  internal val handle: Handle,
) : AbstractCoroutineContextElement(ContextKey) {
  internal companion object ContextKey : CoroutineContext.Key<SqlContext>
}

internal suspend fun getSqlContext(): SqlContext? =
  coroutineContext[SqlContext]
