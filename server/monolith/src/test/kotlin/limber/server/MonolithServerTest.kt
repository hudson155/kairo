package limber.server

import limber.config.ConfigLoader
import limber.config.MonolithServerConfig
import limber.testing.ServerIntegrationTest
import org.junit.jupiter.api.Test

private val config = ConfigLoader.load<MonolithServerConfig>("testing")

internal class MonolithServerTest : ServerIntegrationTest() {
  override val server: Server<*> = MonolithServer(config)

  @Test
  fun start() {
    server.start(wait = false) // Stopped automatically by ServerIntegrationTest.
  }
}
