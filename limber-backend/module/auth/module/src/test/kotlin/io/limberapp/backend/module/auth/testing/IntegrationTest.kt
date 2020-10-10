package io.limberapp.backend.module.auth.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.client.org.OrgRoleClient
import io.limberapp.backend.module.auth.client.org.OrgRoleMembershipClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val orgRoleClient by lazy { OrgRoleClient(httpClient) }
  protected val orgRoleMembershipClient by lazy { OrgRoleMembershipClient(httpClient) }
}
