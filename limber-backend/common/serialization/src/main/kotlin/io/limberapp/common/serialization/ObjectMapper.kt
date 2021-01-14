package io.limberapp.common.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun limberObjectMapper(
    prettyPrint: Boolean = false,
    allowUnknownProperties: Boolean = false,
): ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .setDefaultPrettyPrinter(JsonPrettyPrinter())
    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, prettyPrint)
    .configure(SerializationFeature.INDENT_OUTPUT, prettyPrint)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !allowUnknownProperties)
