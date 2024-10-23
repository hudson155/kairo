package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.json.JsonMapper

public class JsonMapperFactory internal constructor(
  modules: List<Module>,
) : AbstractJsonMapperFactory<JsonMapper, JsonMapper.Builder>(modules) {
  override fun createBuilder(): JsonMapper.Builder {
    val factory = JsonFactory()
    return JsonMapper.Builder(JsonMapper(factory))
  }
}

public fun jsonMapper(
  vararg modules: Module,
  block: JsonMapperFactory.() -> Unit = {},
): JsonMapper =
  JsonMapperFactory(modules.toList()).apply(block).build()
