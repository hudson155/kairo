package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.limberapp.server.Server
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

abstract class AbstractIntegrationTestExtension :
    BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {
  companion object {
    private val engine: TestApplicationEngine = TestApplicationEngine(createTestEnvironment())
    private var started: Boolean = false
  }

  override fun beforeAll(context: ExtensionContext) {
    if (!started) start(context)
  }

  private fun start(context: ExtensionContext) {
    engine.start()
    val server = engine.application.main()
    // TestApplicationEngine does not raise ApplicationStarted, so we have to do it manually.
    engine.environment.monitor.raise(ApplicationStarted, engine.application)
    started = true

    context[Server::class.java] = server
    context[TestApplicationEngine::class.java] = engine
  }

  abstract fun Application.main(): Server<*>

  override fun beforeEach(context: ExtensionContext) {
    val server = context[Server::class.java]
    server.uuidGenerator.reset()
  }

  override fun close() {
    @Suppress("MagicNumber")
    engine.stop(1000, 5000)
  }
}
