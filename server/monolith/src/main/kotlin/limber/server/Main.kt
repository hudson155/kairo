package limber.server

import limber.config.ConfigImpl
import limber.config.ConfigLoader

private val config = ConfigLoader.load<ConfigImpl>(System.getenv("LIMBER_CONFIG"))

internal fun main() {
  MonolithServer(config).start()
}
