package io.limberapp.graphqlServer

import io.ktor.application.Application
import io.limberapp.common.LimberApplication
import io.limberapp.common.module.healthCheck.HealthCheckModule
import io.limberapp.config.ConfigLoader
import io.limberapp.graphqlServer.config.LimberGraphqlServerConfig
import io.limberapp.graphqlServer.module.GraphqlServerModule
import io.limberapp.module.graphql.GraphqlModule

internal class LimberGraphqlServer(
    application: Application,
    configName: String? = null,
) : LimberApplication<LimberGraphqlServerConfig>(
    application = application,
    config = ConfigLoader.load(configName ?: System.getenv("LIMBER_CONFIG"))
) {
  override fun getApplicationModules() = listOf(
      GraphqlServerModule(),
      HealthCheckModule(),
      GraphqlModule(),
  )
}
