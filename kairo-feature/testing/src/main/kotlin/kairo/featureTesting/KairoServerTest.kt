package kairo.featureTesting

import com.google.inject.Injector
import kairo.serverTesting.TestServer
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

public abstract class KairoServerTest {
  public abstract val server: TestServer

  public val injector: Injector
    get() = checkNotNull(server.injector)

  @BeforeEach
  public fun rootBeforeEach(): Unit = runTest {
    server.start(wait = false)
    server.featureManager.features
      .filterIsInstance<TestFeature.BeforeEach>()
      .forEach { it.beforeEach(injector) }
    beforeEach()
  }

  @AfterEach
  public fun rootAfterEach(): Unit = runTest {
    afterEach()
    server.shutDown()
  }

  public open suspend fun beforeEach(): Unit = Unit

  public open suspend fun afterEach(): Unit = Unit
}
