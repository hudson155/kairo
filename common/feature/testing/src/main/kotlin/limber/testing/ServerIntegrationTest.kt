package limber.testing

import io.mockk.clearAllMocks
import limber.feature.TestFeature
import limber.server.Server
import org.junit.jupiter.api.AfterEach

public abstract class ServerIntegrationTest {
  public abstract val server: Server<*>

  @AfterEach
  public fun afterEach() {
    server.features.filterIsInstance<TestFeature>().forEach { it.afterEach() }
    server.stop()
    clearAllMocks()
  }
}
