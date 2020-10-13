package io.limberapp.backend.module.orgs.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.client.feature.FeatureClient
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val featureClient by lazy { FeatureClient(httpClient) }
  protected val orgClient by lazy { OrgClient(httpClient) }
}
