package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.DefaultHeaders

internal fun Application.configureDefaultHeaders() {
  install(DefaultHeaders)
}
