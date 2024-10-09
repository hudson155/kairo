package kairo.rest.contentNegotiation

import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.plugins.contentnegotiation.ContentNegotiationConfig
import kairo.rest.ktorServerMapper

public fun ContentNegotiationConfig.kairoConfigure() {
  register(
    contentType = ContentType.Application.Json,
    converter = JacksonConverter(objectMapper = ktorServerMapper, streamRequestBody = false),
  )
}
