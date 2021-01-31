package io.limberapp.common.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.limberapp.common.typeConversion.TypeConverter

data class LimberObjectMapper(
    private val factory: Factory = Factory.JSON,
    private val typeConverters: Set<TypeConverter<*>> = emptySet(),
    private val allowUnknownProperties: Boolean = false,
    private val prettyPrint: Boolean = false,
) : ObjectMapper(when (factory) {
  Factory.JSON -> null
  Factory.YAML -> YAMLFactory()
}) {
  enum class Factory { JSON, YAML }

  private class JsonPrettyPrinter : DefaultPrettyPrinter() {
    override fun createInstance(): JsonPrettyPrinter = JsonPrettyPrinter()
    override fun writeObjectFieldValueSeparator(jg: JsonGenerator) {
      jg.writeRaw(_separators.objectFieldValueSeparator + " ")
    }
  }

  init {
    // 3rd-party modules.
    registerKotlinModule()
    registerModule(JavaTimeModule())
    // Pretty printer.
    setDefaultPrettyPrinter(JsonPrettyPrinter())
    // Configuration.
    configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, prettyPrint)
    configure(SerializationFeature.INDENT_OUTPUT, prettyPrint)
    configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, prettyPrint)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !allowUnknownProperties)
    // Custom modules.
    if (typeConverters.isNotEmpty()) {
      registerModule(SimpleModule().apply { typeConverters.forEach { registerTypeConverter(it) } })
    }
  }
}

private fun <T : Any, C : TypeConverter<T>> SimpleModule.registerTypeConverter(
    typeConverter: C,
): SimpleModule {
  addSerializer(
      object : StdSerializer<T>(typeConverter.kClass.java) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) {
          gen.writeString(typeConverter.writeString(value))
        }
      })
  addDeserializer(typeConverter.kClass.java,
      object : StdDeserializer<T>(typeConverter.kClass.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T =
            typeConverter.parseString(p.text)
      })
  return this
}
