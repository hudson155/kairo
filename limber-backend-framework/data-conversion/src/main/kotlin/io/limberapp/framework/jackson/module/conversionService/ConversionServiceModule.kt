package io.limberapp.framework.jackson.module.conversionService

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.limberapp.framework.dataConversion.SimpleConversionService

/**
 * This module configures Jackson conversion for the given Ktor conversion service.
 */
class ConversionServiceModule<T : Any>(
    conversionService: SimpleConversionService<T>
) : SimpleModule() {

    init {
        addSerializer(conversionService.clazz.java, serializer(conversionService))
        addDeserializer(conversionService.clazz.java, deserializer(conversionService))
    }

    private fun serializer(conversionService: SimpleConversionService<T>) =
        object : StdSerializer<T>(conversionService.clazz.java) {
            override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) {
                gen.writeString(conversionService.toValue(value))
            }
        }

    private fun deserializer(conversionService: SimpleConversionService<T>) =
        object : StdDeserializer<T>(conversionService.clazz.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
                return conversionService.fromValue(p.valueAsString)
            }
        }
}
