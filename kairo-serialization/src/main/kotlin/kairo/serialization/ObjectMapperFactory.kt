package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module

public object ObjectMapperFactory {
  /**
   * This is the main function to create a Jackson object mapper (well, actually a [JsonFactory]).
   * To configure the object mapper, use [block].
   */
  public fun builder(
    format: ObjectMapperFormat,
    modules: List<Module> = emptyList(),
    block: ObjectMapperFactoryBuilder.() -> Unit = {},
  ): ObjectMapperFactoryBuilder =
    ObjectMapperFactoryBuilder(factory(format), modules, block)

  private fun factory(format: ObjectMapperFormat): JsonFactory =
    when (format) {
      ObjectMapperFormat.Json -> {
        JsonFactory()
      }
    }
}
