package io.limberapp.backend.module.users.testing

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.client.user.UserClient
import io.limberapp.backend.module.TestSqlModule
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.backend.server.test.config.TestConfig
import io.limberapp.common.config.ConfigLoader
import io.limberapp.common.server.Server
import io.limberapp.common.sql.SqlWrapper
import io.limberapp.testing.integration.AbstractIntegrationTest
import io.limberapp.testing.integration.AbstractIntegrationTestExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    limberServer: Server<*>,
) : AbstractIntegrationTest(
    engine = engine,
    server = limberServer,
) {
  internal class Extension : AbstractIntegrationTestExtension() {
    companion object {
      val config: TestConfig = ConfigLoader.load<TestConfig>("test")

      val sqlModule: TestSqlModule = run {
        val sqlDatabaseConfig = config.sqlDatabase
        val sqlWrapper = SqlWrapper(sqlDatabaseConfig)
        return@run TestSqlModule(sqlWrapper, schemaName = "users")
      }
    }

    override fun Application.main(): Server<*> {
      return object : Server<TestConfig>(this, config) {
        override val modules = setOf(UsersModule(), sqlModule)
      }
    }

    override fun beforeEach(context: ExtensionContext) {
      super.beforeEach(context)
      sqlModule.truncateSchema()
    }
  }

  protected val userClient: UserClient by lazy {
    UserClient(httpClient)
  }
}
