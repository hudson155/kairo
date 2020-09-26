package io.limberapp.module.graphql.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer)
