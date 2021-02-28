package io.limberapp.server.monolith

import io.ktor.application.Application
import io.limberapp.config.AuthenticationMechanism
import io.limberapp.config.ConfigLoader
import io.limberapp.config.MonolithConfig
import io.limberapp.module.Module
import io.limberapp.module.clients.ClientsModule
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
  private val clientsModule: ClientsModule = run {
    val internalMechanism = config.authentication.mechanisms
        .filterIsInstance<AuthenticationMechanism.Jwt>()
        .single()
    return@run ClientsModule(config.hosts, internalMechanism)
  }

  override val modules: Set<Module> = setOf(
      clientsModule,
      HealthCheckFeature(HealthCheckServiceImpl::class),
      OrgsFeature(),
      UsersFeature(),
      SqlModule(SqlWrapper(config.sqlDatabase), runMigrations = true),
  )
}
