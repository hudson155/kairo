package limber.rest.ktorPlugins

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.dataconversion.DataConversion

internal fun Application.installHttpPlugins(objectMapper: ObjectMapper) {
  /**
   * https://ktor.io/docs/serialization.html.
   *
   * We use Jackson for serialization. See the serialization library.
   */
  install(ContentNegotiation) {
    register(contentType = ContentType.Application.Json, converter = JacksonConverter(objectMapper))
  }

  install(DataConversion)
}
