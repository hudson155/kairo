package limber.serialization

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLParser
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule

public object ObjectMapperFactory {
  public enum class Format { Json, Yaml }

  public fun builder(format: Format, block: Builder.() -> Unit = {}): Builder =
    Builder(factory(format), block)

  public class Builder internal constructor(
    factory: JsonFactory,
    block: Builder.() -> Unit,
  ) : JsonMapper.Builder(JsonMapper(factory)) {
    public val module: SimpleModule = SimpleModule()

    init {
      addModules(
        kotlinModule {
          configure(KotlinFeature.SingletonSupport, true)
        },
        JavaTimeModule(),
        Jdk8Module().apply {
          configureReadAbsentAsNull(true)
        },
      )

      configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)

      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
      configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
      configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
      configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true)
      configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
      configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)

      block()

      addModule(module)
    }
  }
}

private fun factory(format: ObjectMapperFactory.Format): JsonFactory =
  when (format) {
    ObjectMapperFactory.Format.Json -> {
      JsonFactory()
    }
    ObjectMapperFactory.Format.Yaml -> {
      YAMLFactory()
        .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
        .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
        .configure(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR, true)
        .configure(YAMLParser.Feature.EMPTY_STRING_AS_NULL, false)
    }
  }
