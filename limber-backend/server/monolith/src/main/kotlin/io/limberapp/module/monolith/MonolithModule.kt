package io.limberapp.module.monolith

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import io.limberapp.module.monolith.service.healthCheck.HealthCheckServiceImpl

internal class MonolithModule : ApplicationModule() {
  override val endpoints = emptyList<Class<out ApiEndpoint<*, *, *>>>()

  override fun bindServices() {
    bind(HealthCheckService::class.java).to(HealthCheckServiceImpl::class.java).asEagerSingleton()
  }
}
