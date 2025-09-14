package kairo.sql.postgres

import kairo.exception.LogicalFailure
import kairo.util.firstCauseOf
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage

@Suppress("UseDataClass")
public class ExceptionMapper(
  public val condition: (details: ServerErrorMessage) -> Boolean,
  public val mapper: () -> LogicalFailure,
)

public inline fun <T> withExceptionMappers(
  vararg mappers: ExceptionMapper,
  block: () -> T,
): T {
  try {
    return block()
  } catch (e: ExposedSQLException) {
    val details = e.firstCauseOf<PSQLException>()?.serverErrorMessage
      ?: throw e
    mappers.forEach { mapper ->
      if (mapper.condition(details)) throw mapper.mapper()
    }
    throw e
  }
}

@Suppress("UnderscoresInNumericLiterals")
public fun uniqueViolation(
  constraintName: String,
  block: () -> LogicalFailure,
): ExceptionMapper =
  ExceptionMapper(
    condition = { details ->
      details.sqlState == "23505" &&
        details.constraint == constraintName
    },
    mapper = block,
  )
