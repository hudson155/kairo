package com.piperframework.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.piperframework.dataConversion.DataConversionService

/**
 * This module configures Jackson conversion for the given conversion service.
 */
class ConversionServiceModule<T : Any>(conversionService: DataConversionService<T>) : SimpleModule() {

    init {
        addSerializer(conversionService.klass.java, serializer(conversionService))
        addDeserializer(conversionService.klass.java, deserializer(conversionService))
    }

    private fun serializer(conversionService: DataConversionService<T>) =
        object : StdSerializer<T>(conversionService.klass.java) {
            override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) {
                gen.writeString(conversionService.toString(value))
            }
        }

    private fun deserializer(conversionService: DataConversionService<T>) =
        object : StdDeserializer<T>(conversionService.klass.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
                conversionService.assertValid(p.valueAsString, p.currentName)
                return conversionService.fromString(p.valueAsString)
            }
        }
}