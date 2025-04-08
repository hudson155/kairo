package kairo.sql.store

import org.postgresql.util.ServerErrorMessage

public typealias OnError = (message: ServerErrorMessage) -> Unit
