package com.piperframework.serialization

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.dataConversion.conversionService.UuidConversionService

class Json(prettyPrint: Boolean = false) {

    val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        if (prettyPrint) setDefaultPrettyPrinter(DefaultPrettyPrinter())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        registerModule(ConversionServiceModule(UuidConversionService))
    }

    inline fun <reified T : Any> parse(string: String): T = objectMapper.readValue(string)

    fun <T : Any> stringify(model: T): String = objectMapper.writeValueAsString(model)
}
