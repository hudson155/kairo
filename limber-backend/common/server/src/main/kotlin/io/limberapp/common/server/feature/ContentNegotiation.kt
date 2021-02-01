package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.limberapp.common.serialization.LimberObjectMapper

internal fun Application.configureContentNegotiation(objectMapper: LimberObjectMapper) {
  install(ContentNegotiation) {
    register(
        contentType = ContentType.Application.Json,
        converter = JacksonConverter(objectMapper),
    )
  }
}
