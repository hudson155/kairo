package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.feature.FeatureClient
import io.limberapp.client.org.OrgClient
import io.limberapp.config.ConfigLoader
import io.limberapp.config.TestConfig
import io.limberapp.module.orgs.OrgsFeature
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
          TestSqlModule(SqlWrapper(config.sqlDatabase), schemaName = "orgs")
    }

    override fun Application.main(): Server<*> {
      return object : Server<TestConfig>(this, config) {
        override val modules = setOf(OrgsFeature(), sqlModule)
      }
    }

    override fun beforeEach(context: ExtensionContext) {
      super.beforeEach(context)
      sqlModule.truncateSchema()
    }
  }

  protected val featureClient: FeatureClient by lazy {
    FeatureClient(httpClient)
  }

  protected val orgClient: OrgClient by lazy {
    OrgClient(httpClient)
  }
}
