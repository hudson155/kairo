package io.limberapp.module.graphql

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import io.limberapp.module.graphql.service.healthCheck.HealthCheckServiceImpl

internal class GraphqlModule : ApplicationModule() {
  override val endpoints = emptyList<Class<out ApiEndpoint<*, *, *>>>()

  override fun bindServices() {
    bind(HealthCheckService::class, HealthCheckServiceImpl::class)
  }
}
