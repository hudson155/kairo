package io.limberapp.backend.module.healthCheck

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.client.healthCheck.HealthCheckClient
import io.limberapp.backend.server.test.config.TestConfig
import io.limberapp.backend.service.healthCheck.TestHealthCheckService
import io.limberapp.common.config.ConfigLoader
import io.limberapp.common.server.Server
import io.limberapp.testing.integration.LimberIntegrationTest
import io.limberapp.testing.integration.LimberIntegrationTestExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : LimberIntegrationTest(engine, server) {
  internal class Extension : LimberIntegrationTestExtension() {
    override fun Application.main(): Server<TestConfig> = Server(
        application = this,
        modules = listOf(HealthCheckFeature(TestHealthCheckService::class)),
        config = ConfigLoader.load("test"),
    )
  }

  protected val healthCheckClient: HealthCheckClient by lazy {
    HealthCheckClient(httpClient)
  }
}
