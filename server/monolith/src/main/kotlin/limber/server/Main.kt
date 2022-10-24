package limber.server

import limber.config.ConfigLoader
import limber.config.MonolithServerConfig

private val config = ConfigLoader.load<MonolithServerConfig>(System.getenv("LIMBER_CONFIG"))

internal fun main() {
  MonolithServer(config).start()
}
