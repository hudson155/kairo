package kairo.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLParser

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
      ObjectMapperFormat.Json -> jsonFactory()
      ObjectMapperFormat.Yaml -> yamlFactory()
    }

  private fun jsonFactory(): JsonFactory =
    JsonFactory()

  private fun yamlFactory(): JsonFactory =
    YAMLFactory()
      .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
      .configure(YAMLGenerator.Feature.USE_NATIVE_OBJECT_ID, false)
      .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
      .configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true)
      .configure(YAMLGenerator.Feature.INDENT_ARRAYS, true)
      .configure(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR, true)
      .configure(YAMLParser.Feature.EMPTY_STRING_AS_NULL, false)
      .configure(YAMLParser.Feature.PARSE_BOOLEAN_LIKE_WORDS_AS_STRINGS, true)
}
