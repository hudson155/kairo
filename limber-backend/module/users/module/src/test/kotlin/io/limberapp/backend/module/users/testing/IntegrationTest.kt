package io.limberapp.backend.module.users.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.users.client.account.UserClient
import io.limberapp.backend.module.users.client.account.UserRoleClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val userClient by lazy { UserClient(httpClient) }
  protected val userRoleClient by lazy { UserRoleClient(httpClient) }
}
