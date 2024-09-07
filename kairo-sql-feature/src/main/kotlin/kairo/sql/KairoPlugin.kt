package kairo.sql

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.argument.ArgumentFactory
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.spi.JdbiPlugin

internal class KairoPlugin : JdbiPlugin {
  override fun customizeJdbi(jdbi: Jdbi) {
    jdbi.register(
      columnMapper = UuidColumnMapper(),
      argumentFactory = UuidArgumentFactory(),
    )
  }

  private fun Jdbi.register(
    columnMapper: ColumnMapper<*>,
    argumentFactory: ArgumentFactory,
  ) {
    registerColumnMapper(columnMapper)
    registerArgument(argumentFactory)
  }
}
