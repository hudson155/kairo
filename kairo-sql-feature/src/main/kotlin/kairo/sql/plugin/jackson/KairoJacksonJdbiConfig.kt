package kairo.sql.plugin.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.jdbi.v3.core.config.JdbiConfig

public class KairoJacksonJdbiConfig(
  public var readMapper: ObjectMapper = ObjectMapper(),
  public var writeMapper: ObjectMapper = ObjectMapper(),
) : JdbiConfig<KairoJacksonJdbiConfig> {
  override fun createCopy(): KairoJacksonJdbiConfig? {
    return KairoJacksonJdbiConfig(readMapper, writeMapper)
  }
}
