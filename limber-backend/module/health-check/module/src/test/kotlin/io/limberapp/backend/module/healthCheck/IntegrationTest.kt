package io.limberapp.backend.module.healthCheck

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.client.healthCheck.HealthCheckClient
import io.limberapp.backend.server.test.config.TestConfig
import io.limberapp.backend.service.healthCheck.TestHealthCheckService
import io.limberapp.common.config.ConfigLoader
import io.limberapp.common.module.Module
import io.limberapp.common.server.Server
import io.limberapp.testing.integration.AbstractIntegrationTest
import io.limberapp.testing.integration.AbstractIntegrationTestExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : AbstractIntegrationTest(engine, server) {
  internal class Extension : AbstractIntegrationTestExtension() {
    override fun Application.main(): Server<TestConfig> = object : Server<TestConfig>(
        application = this,
        config = ConfigLoader.load("test"),
    ) {
      override val modules: Set<Module> = setOf(HealthCheckFeature(TestHealthCheckService::class))
    }
  }

  protected val healthCheckClient: HealthCheckClient by lazy {
    HealthCheckClient(httpClient)
  }
}
