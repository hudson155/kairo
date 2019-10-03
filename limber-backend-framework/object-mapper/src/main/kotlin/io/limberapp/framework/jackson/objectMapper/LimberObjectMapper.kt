package io.limberapp.framework.jackson.objectMapper

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.limberapp.framework.dataConversion.conversionService.GuidConversionService
import io.limberapp.framework.jackson.module.conversionService.ConversionServiceModule

/**
 * Custom ObjectMapper configured for Kotlin, pretty printing, custom datatype conversion, etc.
 */
open class LimberObjectMapper(prettyPrint: Boolean) : ObjectMapper() {

    init {
        registerKotlinModule()
        if (prettyPrint) configurePrettyPrinting()
        registerDefaultModules()
    }

    private fun registerKotlinModule() {
        registerModule(KotlinModule())
    }

    private fun configurePrettyPrinting() {
        setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            indentObjectsWith(DefaultIndenter("  ", "\n"))
        })
    }

    private fun registerDefaultModules() {

        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        registerModule(ConversionServiceModule(GuidConversionService()))
    }
}
