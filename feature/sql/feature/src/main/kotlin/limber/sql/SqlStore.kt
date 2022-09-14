package limber.sql

import com.google.common.io.Resources
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

public abstract class SqlStore(protected val jdbi: Jdbi) {
  @Suppress("FunctionMinLength") // Short for the purpose of brevity due to frequent reuse.
  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()
}

public fun <R> Jdbi.transaction(callback: (Handle) -> R): R =
  inTransaction<R, Exception>(callback)

public fun <R> Jdbi.handle(callback: (Handle) -> R): R =
  withHandle<R, Exception>(callback)
