package limber.server

import limber.config.ConfigImpl
import limber.config.ConfigLoader
import limber.testing.ServerIntegrationTest
import org.junit.jupiter.api.Test

private val config = ConfigLoader.load<ConfigImpl>("development")

internal class MonolithServerTest : ServerIntegrationTest() {
  override val server: Server<*> = MonolithServer(config)

  @Test
  fun start() {
    server.start(wait = false) // Stopped automatically by ServerIntegrationTest.
  }
}
