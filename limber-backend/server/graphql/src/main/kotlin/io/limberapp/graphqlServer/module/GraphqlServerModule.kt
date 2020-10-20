package io.limberapp.graphqlServer.module

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import io.limberapp.graphqlServer.module.service.healthCheck.HealthCheckServiceImpl

internal class GraphqlServerModule : ApplicationModule() {
  override val endpoints = emptyList<Class<out ApiEndpoint<*, *>>>()

  override fun bindServices() {
    bind(HealthCheckService::class.java).to(HealthCheckServiceImpl::class.java).asEagerSingleton()
  }
}
