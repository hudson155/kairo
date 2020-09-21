package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

abstract class IntegrationTestExtension :
  BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {
  companion object {
    private val engine = TestApplicationEngine(createTestEnvironment())
    private var started = false
  }

  override fun beforeAll(context: ExtensionContext) {
    if (!started) {
      with(engine) {
        start()
        application.main()
        // TestApplicationEngine does not raise ApplicationStarted.
        environment.monitor.raise(ApplicationStarted, application)
      }
      started = true
      with(context.root.getStore(ExtensionContext.Namespace.GLOBAL)) {
        put("TEST_APPLICATION_ENGINE", engine)
        put("YOUR_EXTENSION", this)
      }
    }
  }

  abstract fun Application.main()

  override fun beforeEach(context: ExtensionContext) {
    MockKAnnotations.init(context.requiredTestClass)
  }

  @Suppress("MagicNumber")
  override fun close() {
    engine.stop(3000, 5000)
  }
}
