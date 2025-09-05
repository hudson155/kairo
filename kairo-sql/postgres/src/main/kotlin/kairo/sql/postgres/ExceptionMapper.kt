package kairo.sql.postgres

import io.r2dbc.postgresql.api.ErrorDetails
import io.r2dbc.postgresql.api.PostgresqlException
import kairo.exception.LogicalFailure
import kairo.util.firstCauseOf
import kotlin.jvm.optionals.getOrNull
import org.jetbrains.exposed.v1.r2dbc.ExposedR2dbcException

@Suppress("UseDataClass")
public class ExceptionMapper(
  public val condition: (details: ErrorDetails) -> Boolean,
  public val mapper: () -> LogicalFailure.Properties,
)

public inline fun <T> withExceptionMappers(
  vararg mappers: ExceptionMapper,
  block: () -> T,
): T {
  try {
    return block()
  } catch (e: ExposedR2dbcException) {
    val details = e.firstCauseOf<PostgresqlException>()?.errorDetails
      ?: throw e
    mappers.forEach { mapper ->
      if (mapper.condition(details)) throw LogicalFailure(mapper.mapper())
    }
    throw e
  }
}

public fun uniqueViolation(
  constraintName: String,
  block: () -> LogicalFailure.Properties,
): ExceptionMapper =
  ExceptionMapper(
    condition = { details ->
      details.code == "23505"
        && details.constraintName.getOrNull() == constraintName
    },
    mapper = block,
  )
