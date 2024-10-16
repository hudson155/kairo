package kairo.sql.plugin.kairo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.argument.ArgumentFactory
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.spi.JdbiPlugin

/**
 * Intended to extend JDBI with Kairo-specific functionality.
 * This currently means adding support for various types.
 */
internal class KairoPlugin : JdbiPlugin {
  override fun customizeJdbi(jdbi: Jdbi) {
    jdbi.register(KairoIdColumnMapper(), KairoIdArgumentFactory())
    jdbi.register(ProtectedStringColumnMapper(), ProtectedStringArgumentFactory())
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
