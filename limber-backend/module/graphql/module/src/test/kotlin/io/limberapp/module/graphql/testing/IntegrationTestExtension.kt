package io.limberapp.module.graphql.testing

import io.ktor.application.Application
import io.limberapp.common.LimberApplication
import io.limberapp.config.Config
import io.limberapp.config.authentication.AuthenticationConfig
import io.limberapp.config.authentication.AuthenticationMechanism
import io.limberapp.config.authentication.ClockConfig
import io.limberapp.config.authentication.UuidsConfig
import io.limberapp.module.graphql.GraphqlModule
import io.limberapp.testing.integration.IntegrationTestExtension

internal class IntegrationTestExtension : IntegrationTestExtension() {
  private val config = object : Config {
    override val authentication = AuthenticationConfig(listOf(AuthenticationMechanism.UnsignedJwt(leeway = 0)))
    override val clock = ClockConfig(ClockConfig.Type.FIXED)
    override val uuids = UuidsConfig(UuidsConfig.Generation.DETERMINISTIC)
  }

  override fun Application.main() {
    object : LimberApplication<Config>(this, config) {
      override fun getApplicationModules() = listOf(GraphqlModule())
    }
  }
}
