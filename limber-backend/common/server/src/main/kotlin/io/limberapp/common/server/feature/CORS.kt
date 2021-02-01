package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

internal fun Application.configureCors() {
  install(CORS) {
    HttpMethod.DefaultMethods.forEach { method(it) }
    allowSameOrigin = false
    anyHost()
    header(HttpHeaders.Authorization)
    header(HttpHeaders.ContentType)
  }
}
