package kairo.sql.plugin.jackson

import kairo.sql.sqlReadMapper
import kairo.sql.sqlWriteMapper
import kotlin.jvm.java
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.json.JsonConfig
import org.jdbi.v3.json.JsonPlugin

internal class JacksonPlugin : JdbiPlugin.Singleton() {
  override fun customizeJdbi(jdbi: Jdbi) {
    jdbi.installPlugin(JsonPlugin())
    jdbi.getConfig(JsonConfig::class.java)
      .setJsonMapper(KairoJacksonJsonMapper())
  }

  override fun customizeHandle(handle: Handle): Handle =
    super.customizeHandle(handle).apply {
      getConfig(KairoJacksonJdbiConfig::class.java).readMapper = sqlReadMapper
      getConfig(KairoJacksonJdbiConfig::class.java).writeMapper = sqlWriteMapper
    }
}
