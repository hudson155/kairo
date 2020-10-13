package io.limberapp.common.sql

import org.jdbi.v3.core.argument.Argument
import org.jdbi.v3.core.argument.NamedArgumentFinder
import org.jdbi.v3.core.argument.NullArgument
import org.jdbi.v3.core.statement.SqlStatement
import org.jdbi.v3.core.statement.StatementContext
import java.util.*

private object NullArgumentFinder : NamedArgumentFinder {
  override fun find(name: String, ctx: StatementContext): Optional<Argument> = Optional.of(NullArgument(null))
}

fun <This : SqlStatement<This>> SqlStatement<This>.bindNullForMissingArguments(): This =
    bindNamedArgumentFinder(NullArgumentFinder)
