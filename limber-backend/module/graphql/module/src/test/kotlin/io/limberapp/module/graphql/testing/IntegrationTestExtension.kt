package io.limberapp.module.graphql.testing

import io.ktor.application.Application
import io.limberapp.common.LimberApplication
import io.limberapp.common.config.ConfigLoader
import io.limberapp.module.graphql.GraphqlModule
import io.limberapp.module.graphql.config.GraphqlModuleConfig
import io.limberapp.testing.integration.LimberIntegrationTestExtension

internal class IntegrationTestExtension : LimberIntegrationTestExtension() {
  companion object {
    private val config = ConfigLoader.load<GraphqlModuleConfig>("test")
  }

  override fun Application.main() = object : LimberApplication<GraphqlModuleConfig>(this, config) {
    override fun getApplicationModules() = listOf(GraphqlModule())
  }
}
