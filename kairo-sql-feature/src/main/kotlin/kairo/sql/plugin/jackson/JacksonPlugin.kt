package kairo.sql.plugin.jackson

import kairo.sql.sqlMapper
import org.jdbi.v3.core.Handle
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin

internal class JacksonPlugin : Jackson2Plugin() {
  override fun customizeHandle(handle: Handle): Handle =
    super.customizeHandle(handle).apply {
      getConfig(Jackson2Config::class.java).mapper = sqlMapper
    }
}
