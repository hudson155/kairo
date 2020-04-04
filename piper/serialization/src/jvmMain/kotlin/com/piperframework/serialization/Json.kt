package com.piperframework.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.dataConversion.DataConversionService
import com.piperframework.dataConversion.conversionService.UuidConversionService

class Json(prettyPrint: Boolean = false) {

    val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        if (prettyPrint) setDefaultPrettyPrinter(DefaultPrettyPrinter())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        registerModule(
            ConversionServiceModule(
                UuidConversionService
            )
        )
    }

    inline fun <reified T : Any> parse(string: String): T = objectMapper.readValue(string)

    fun <T : Any> stringify(model: T): String = objectMapper.writeValueAsString(model)
}

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
