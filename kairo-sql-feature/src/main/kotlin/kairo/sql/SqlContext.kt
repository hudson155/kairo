package kairo.sql

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import org.jdbi.v3.core.Handle

public class SqlContext(
  internal val handle: Handle,
) : AbstractCoroutineContextElement(SqlContext) {
  public companion object : CoroutineContext.Key<SqlContext>
}
