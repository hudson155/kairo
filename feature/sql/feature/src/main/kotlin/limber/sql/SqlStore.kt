package limber.sql

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

public abstract class SqlStore(protected val jdbi: Jdbi)

public fun <R> Jdbi.transaction(callback: (Handle) -> R): R =
  inTransaction<R, Exception>(callback)

public fun <R> Jdbi.handle(callback: (Handle) -> R): R =
  withHandle<R, Exception>(callback)
