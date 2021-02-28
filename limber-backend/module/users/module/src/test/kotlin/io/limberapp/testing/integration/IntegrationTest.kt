package io.limberapp.testing.integration

import io.ktor.application.Application
import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.client.user.UserClient
import io.limberapp.config.ConfigLoader
import io.limberapp.config.TestConfig
import io.limberapp.module.sql.TestSqlModule
import io.limberapp.module.users.UsersFeature
import io.limberapp.server.Server
import io.limberapp.sql.SqlWrapper
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(IntegrationTest.Extension::class)
internal abstract class IntegrationTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : AbstractIntegrationTest(
    engine = engine,
    server = server,
) {
  internal class Extension : AbstractIntegrationTestExtension() {
    companion object {
      val config: TestConfig = ConfigLoader.load("test")
      val sqlModule: TestSqlModule =
          TestSqlModule(SqlWrapper(config.sqlDatabase), schemaName = "users")
    }

    override fun Application.main(): Server<*> {
      return object : Server<TestConfig>(this, config) {
        override val modules = setOf(UsersFeature(), sqlModule, MockModule)
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
