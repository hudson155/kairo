package io.limberapp.server.monolith

import io.ktor.application.Application
import io.limberapp.config.ConfigLoader
import io.limberapp.config.MonolithConfig
import io.limberapp.module.Module
import io.limberapp.module.healthCheck.HealthCheckFeature
import io.limberapp.module.orgs.OrgsFeature
import io.limberapp.module.sql.SqlModule
import io.limberapp.module.users.UsersFeature
import io.limberapp.server.Server
import io.limberapp.service.healthCheck.HealthCheckServiceImpl
import io.limberapp.sql.SqlWrapper

internal fun Application.main() {
  MonolithServer(this)
}

internal class MonolithServer(application: Application) : Server<MonolithConfig>(
    application = application,
    config = ConfigLoader.load(System.getenv("LIMBER_CONFIG")),
) {
  override val modules: Set<Module> = setOf(
      HealthCheckFeature(HealthCheckServiceImpl::class),
      OrgsFeature(),
      UsersFeature(),
      SqlModule(SqlWrapper(config.sqlDatabase), runMigrations = false),
  )
}
