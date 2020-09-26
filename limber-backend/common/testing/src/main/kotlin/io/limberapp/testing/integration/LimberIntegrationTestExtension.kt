package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.limberapp.common.LimberApplication
import io.mockk.clearAllMocks
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

abstract class LimberIntegrationTestExtension :
  BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {
  companion object {
    private val engine = TestApplicationEngine(createTestEnvironment())
    private var started = false
  }

  override fun beforeAll(context: ExtensionContext) {
    if (!started) {
      val limberServer = with(engine) {
        start()
        return@with application.main().also {
          // TestApplicationEngine does not raise ApplicationStarted.
          environment.monitor.raise(ApplicationStarted, application)
        }
      }
      started = true
      with(context.root.getStore(ExtensionContext.Namespace.GLOBAL)) {
        put(TEST_CONTEXT[LimberApplication::class.java], limberServer)
        put(TEST_CONTEXT[TestApplicationEngine::class.java], engine)
        put(TEST_CONTEXT[LimberIntegrationTestExtension::class.java], this)
      }
    }
  }

  abstract fun Application.main(): LimberApplication<*>

  override fun beforeEach(context: ExtensionContext) {
    val limberServer = context[LimberApplication::class.java]
    limberServer.uuidGenerator.reset()
    clearAllMocks()
  }

  @Suppress("MagicNumber")
  override fun close() {
    engine.stop(3000, 5000)
  }
}
