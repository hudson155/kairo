package kairo.sql.postgres

import kairo.exception.LogicalFailure
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

@Suppress("UseDataClass")
public class ExceptionMapper(
  public val condition: (details: Nothing) -> Boolean,
  public val mapper: () -> LogicalFailure,
)

public inline fun <T> withExceptionMappers(
  vararg mappers: ExceptionMapper,
  block: () -> T,
): T {
  try {
    return block()
  } catch (e: ExposedSQLException) {
    TODO()
    // val details = e.firstCauseOf<PostgresqlException>()?.errorDetails
    //   ?: throw e
    // mappers.forEach { mapper ->
    //   if (mapper.condition(details)) throw mapper.mapper()
    // }
    // throw e
  }
}

public fun uniqueViolation(
  constraintName: String,
  block: () -> LogicalFailure,
): ExceptionMapper =
  TODO()
// ExceptionMapper(
//   condition = { details ->
//     details.code == "23505" &&
//       details.constraintName.getOrNull() == constraintName
//   },
//   mapper = block,
// )
