package io.limberapp.common.module.healthCheck

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.healthCheck.endpoint.healthCheck.HealthCheck

class HealthCheckModule : ApplicationModule() {
  override val endpoints = listOf<Class<out ApiEndpoint<*, *>>>(HealthCheck::class.java)

  override fun bindServices() = Unit
}
