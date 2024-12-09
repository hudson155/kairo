package kairo.sql.plugin.jackson

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties
import org.jdbi.v3.core.Handle
import org.jdbi.v3.jackson2.Jackson2Config
import org.jdbi.v3.jackson2.Jackson2Plugin

internal class JacksonPlugin : Jackson2Plugin() {
  private val mapper: JsonMapper =
    jsonMapper {
      allowUnknownProperties = true
    }.build()

  override fun customizeHandle(handle: Handle): Handle =
    super.customizeHandle(handle).apply {
      getConfig(Jackson2Config::class.java).setMapper(mapper)
    }
}
