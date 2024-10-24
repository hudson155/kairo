package kairo.rest.server

import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kairo.rest.KtorServerMapper

internal fun Application.installContentNegotiation() {
  install(ContentNegotiation) {
    register(
      contentType = ContentType.Application.Json,
      converter = JacksonConverter(objectMapper = KtorServerMapper.json, streamRequestBody = false),
    )
  }
}
