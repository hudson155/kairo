package limber.rest.ktorPlugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse

internal fun Application.installRoutingPlugins() {
  install(AutoHeadResponse)
}
