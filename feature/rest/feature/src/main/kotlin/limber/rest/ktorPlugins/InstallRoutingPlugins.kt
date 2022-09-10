package limber.rest.ktorPlugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.doublereceive.DoubleReceive

internal fun Application.installRoutingPlugins() {
  install(AutoHeadResponse)

  install(DoubleReceive)
}
