package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.json.JsonMapper

public class JsonMapperFactory internal constructor() :
  AbstractJsonMapperFactory<JsonMapper, JsonMapper.Builder>() {
  override fun createBuilder(): JsonMapper.Builder {
    val factory = JsonFactory()
    return JsonMapper.Builder(JsonMapper(factory))
  }
}

public fun jsonMapper(configure: JsonMapperFactory.() -> Unit = {}): JsonMapperFactory =
  JsonMapperFactory().apply(configure)
