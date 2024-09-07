package kairo.sql

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.argument.ArgumentFactory
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.spi.JdbiPlugin

/**
 * Intended to extend JDBI with Kairo-specific functionality.
 * This currently only means
 */
internal class KairoPlugin : JdbiPlugin {
  override fun customizeJdbi(jdbi: Jdbi) {
    jdbi.register(UuidColumnMapper(), UuidArgumentFactory())
  }

  /**
   * We typically want to register both a [ColumnMapper] and an [ArgumentFactory] for a specific type.
   * This util helps us avoid registering one but not the other.
   */
  private fun Jdbi.register(columnMapper: ColumnMapper<*>, argumentFactory: ArgumentFactory) {
    registerColumnMapper(columnMapper)
    registerArgument(argumentFactory)
  }
}
