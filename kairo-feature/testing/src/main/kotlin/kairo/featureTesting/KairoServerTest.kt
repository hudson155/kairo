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
  public open fun beforeEach(): Unit = runTest {
    server.start(wait = false)
    server.featureManager.features.forEach { feature ->
      if (feature is TestFeature.BeforeEach) {
        feature.beforeEachTest(injector)
      }
    }
  }

  @AfterEach
  public open fun afterEach(): Unit = runTest {
    server.shutDown()
  }
}
