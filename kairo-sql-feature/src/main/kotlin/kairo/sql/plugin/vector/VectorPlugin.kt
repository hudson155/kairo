package kairo.sql.plugin.vector

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.spi.JdbiPlugin

internal class VectorPlugin : JdbiPlugin {
  override fun customizeJdbi(jdbi: Jdbi) {
    jdbi.registerColumnMapper(VectorColumnMapperFactory())
    jdbi.registerArgument(VectorArgumentFactory())
  }
}
