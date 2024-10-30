package kairo.rest.server

import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kairo.rest.ktorServerMapper

internal fun Application.installContentNegotiation() {
  install(ContentNegotiation) {
    register(
      contentType = ContentType.Application.Json,
      converter = JacksonConverter(objectMapper = ktorServerMapper, streamRequestBody = false),
    )
  }
}
