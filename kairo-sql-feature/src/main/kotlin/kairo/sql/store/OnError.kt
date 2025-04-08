package kairo.sql.store

import org.postgresql.util.ServerErrorMessage

/**
 * Helps individual [SqlStore] methods customize error handling.
 */
public typealias OnError = (message: ServerErrorMessage) -> Unit
