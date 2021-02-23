package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.healthCheck.HealthCheckClient
import io.limberapp.config.ConfigLoader
import io.limberapp.config.TestConfig
import io.limberapp.module.Module
import io.limberapp.module.healthCheck.HealthCheckFeature
import io.limberapp.server.Server
import io.limberapp.service.healthCheck.TestHealthCheckService
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
