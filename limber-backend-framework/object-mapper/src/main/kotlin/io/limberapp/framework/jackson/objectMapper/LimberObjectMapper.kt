package io.limberapp.framework.jackson.objectMapper

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.limberapp.framework.dataConversion.conversionService.UuidConversionService
import io.limberapp.framework.jackson.module.conversionService.ConversionServiceModule

/**
 * Custom ObjectMapper configured for Kotlin, pretty printing, custom datatype conversion, etc. This should be used
 * across Limber.
 */
open class LimberObjectMapper(
    jsonFactory: JsonFactory? = null,
    prettyPrint: Boolean = false
) : ObjectMapper(jsonFactory) {

    init {
        registerKotlinModule()
        if (prettyPrint) configurePrettyPrinting()
        ignoreUnknownProperties()
        registerDefaultModules()
    }

    private fun registerKotlinModule() {
        registerModule(KotlinModule())
    }

    private fun configurePrettyPrinting() {
        setDefaultPrettyPrinter(DefaultPrettyPrinter())
    }

    private fun ignoreUnknownProperties() {
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private fun registerDefaultModules() {

        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        registerModule(ConversionServiceModule(UuidConversionService()))
    }
}
