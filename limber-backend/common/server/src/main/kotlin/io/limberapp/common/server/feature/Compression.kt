package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.Compression

internal fun Application.configureCompression() {
  install(Compression)
}
