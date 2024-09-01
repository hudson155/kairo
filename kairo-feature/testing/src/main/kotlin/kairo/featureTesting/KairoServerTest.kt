package kairo.featureTesting

import com.google.inject.Injector
import kairo.server.Server
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

public abstract class KairoServerTest {
  public abstract val server: Server

  public val injector: Injector
    get() = checkNotNull(server.injector)

  @BeforeEach
  public open fun beforeEach() {
    server.start(wait = false)
  }

  @AfterEach
  public open fun afterEach() {
    server.shutDown()
  }
}
