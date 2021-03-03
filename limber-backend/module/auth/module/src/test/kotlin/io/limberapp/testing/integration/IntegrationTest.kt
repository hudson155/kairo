package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.feature.FeatureRoleClient
import io.limberapp.client.jwtClaimsRequest.JwtClaimsRequestClient
import io.limberapp.client.org.OrgRoleClient
import io.limberapp.client.org.OrgRoleMembershipClient
import io.limberapp.client.tenant.TenantClient
import io.limberapp.client.tenant.TenantDomainClient
import io.limberapp.config.ConfigLoader
import io.limberapp.config.TestConfig
import io.limberapp.module.auth.AuthFeature
import io.limberapp.module.sql.TestSqlModule
import io.limberapp.server.Server
import io.limberapp.sql.SqlWrapper
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : AbstractIntegrationTest(engine, server) {
  internal class Extension : AbstractIntegrationTestExtension() {
    companion object {
      val config: TestConfig = ConfigLoader.load("test")
      val sqlModule: TestSqlModule =
          TestSqlModule(SqlWrapper(config.sqlDatabase), schemaName = "auth")
    }

    override fun Application.main(): Server<*> {
      return object : Server<TestConfig>(this, config) {
        override val modules = setOf(AuthFeature(), sqlModule, MockModule, TypeConversionModule)
      }
    }

    override fun beforeEach(context: ExtensionContext) {
      super.beforeEach(context)
      sqlModule.truncateSchema()
    }
  }

  protected val featureRoleClient: FeatureRoleClient by lazy {
    FeatureRoleClient(httpClient)
  }

  protected val jwtClaimsRequestClient: JwtClaimsRequestClient by lazy {
    JwtClaimsRequestClient(httpClient)
  }

  protected val orgRoleClient: OrgRoleClient by lazy {
    OrgRoleClient(httpClient)
  }

  protected val orgRoleMembershipClient: OrgRoleMembershipClient by lazy {
    OrgRoleMembershipClient(httpClient)
  }

  protected val tenantClient: TenantClient by lazy {
    TenantClient(httpClient)
  }

  protected val tenantDomainClient: TenantDomainClient by lazy {
    TenantDomainClient(httpClient)
  }
}
