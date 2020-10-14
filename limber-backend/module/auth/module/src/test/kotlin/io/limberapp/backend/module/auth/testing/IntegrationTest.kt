package io.limberapp.backend.module.auth.testing

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.client.feature.FeatureRoleClient
import io.limberapp.backend.module.auth.client.jwtClaimsRequest.JwtClaimsRequestClient
import io.limberapp.backend.module.auth.client.org.OrgRoleClient
import io.limberapp.backend.module.auth.client.org.OrgRoleMembershipClient
import io.limberapp.backend.module.auth.client.tenant.TenantClient
import io.limberapp.common.LimberApplication
import io.limberapp.testing.integration.LimberIntegrationTest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(IntegrationTestExtension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : LimberIntegrationTest(engine, limberServer) {
  protected val featureRoleClient by lazy { FeatureRoleClient(httpClient) }
  protected val jwtClaimsRequestClient by lazy { JwtClaimsRequestClient(httpClient) }
  protected val orgRoleClient by lazy { OrgRoleClient(httpClient) }
  protected val orgRoleMembershipClient by lazy { OrgRoleMembershipClient(httpClient) }
  protected val tenantClient by lazy { TenantClient(httpClient) }
}
