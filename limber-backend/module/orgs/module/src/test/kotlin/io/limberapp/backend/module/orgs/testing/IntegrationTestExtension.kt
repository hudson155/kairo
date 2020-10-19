package io.limberapp.backend.module.orgs.testing

import io.ktor.application.Application
import io.limberapp.backend.module.TestSqlModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.common.LimberApplication
import io.limberapp.common.config.ConfigLoader
import io.limberapp.monolith.config.LimberMonolithConfig
import io.limberapp.testing.integration.LimberIntegrationTestExtension
import org.junit.jupiter.api.extension.ExtensionContext

internal class IntegrationTestExtension : LimberIntegrationTestExtension() {
  companion object {
    private val config = ConfigLoader.load<LimberMonolithConfig>("test")
    private val sqlModule = TestSqlModule(config.sqlDatabase)
  }

  override fun beforeEach(context: ExtensionContext) {
    super.beforeEach(context)
    sqlModule.dropDatabase()
  }

  override fun Application.main() = object : LimberApplication<LimberMonolithConfig>(this, config) {
    override fun getApplicationModules() = listOf(OrgsModule())
    override fun getAdditionalModules() = listOf(MockModule(), sqlModule)
  }
}
