package io.limberapp.backend.module.healthCheck

import io.limberapp.backend.endpoint.healthCheck.HealthCheck
import io.limberapp.common.module.Feature
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.backend.service.healthCheck.HealthCheckService
import kotlin.reflect.KClass

class HealthCheckFeature(private val implClass: KClass<out HealthCheckService>) : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(HealthCheck::class)

  override fun bind() {
    bind(HealthCheckService::class.java).to(implClass.java).asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
