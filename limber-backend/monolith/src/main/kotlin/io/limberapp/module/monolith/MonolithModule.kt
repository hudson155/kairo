package io.limberapp.module.monolith

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.Module
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import io.limberapp.module.monolith.service.healthCheck.HealthCheckServiceImpl

internal class MonolithModule : Module() {
  override val endpoints = emptyList<Class<out ApiEndpoint<*, *, *>>>()

  override fun bindServices() {
    bind(HealthCheckService::class, HealthCheckServiceImpl::class)
  }
}
