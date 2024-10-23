package kairo.serialization

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLParser

public class YamlMapperFactory internal constructor(
  modules: List<Module>,
) : AbstractJsonMapperFactory<YAMLMapper, YAMLMapper.Builder>(modules) {
  override fun createBuilder(): YAMLMapper.Builder {
    val factory = YAMLFactory()
      .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
      .configure(YAMLGenerator.Feature.USE_NATIVE_OBJECT_ID, false)
      .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
      .configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true)
      .configure(YAMLGenerator.Feature.INDENT_ARRAYS, true)
      .configure(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR, true)
      .configure(YAMLParser.Feature.EMPTY_STRING_AS_NULL, false)
      .configure(YAMLParser.Feature.PARSE_BOOLEAN_LIKE_WORDS_AS_STRINGS, true)
    return YAMLMapper.Builder(YAMLMapper(factory))
  }
}

public fun yamlMapper(
  vararg modules: Module,
  block: YamlMapperFactory.() -> Unit = {},
): YAMLMapper =
  YamlMapperFactory(modules.toList()).apply(block).build()
