package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.user.UserClient
import io.limberapp.config.ConfigLoader
import io.limberapp.config.TestConfig
import io.limberapp.module.sql.TestSqlModule
import io.limberapp.module.users.UsersModule
import io.limberapp.server.Server
import io.limberapp.sql.SqlWrapper
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
