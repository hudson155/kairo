package io.limberapp.server.feature

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import org.slf4j.event.Level

internal fun Application.configureCallLogging() {
  install(CallLogging) {
    level = Level.INFO
  }
}
