package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.json.JsonMapper

public class ObjectMapperFactoryBuilder internal constructor(
  factory: JsonFactory,
  modules: List<Module>,
  block: ObjectMapperFactoryBuilder.() -> Unit,
) : JsonMapper.Builder(JsonMapper(factory)) {
  public var allowUnknownProperties: Boolean = false

  init {
    block()

    increaseStrictness()
    configurePrettyPrinting()
    setUnknownPropertyHandling()

    addModules(
      createKotlinModule(),
      createStringModule(),
    )

    addModules(modules)
  }
}
