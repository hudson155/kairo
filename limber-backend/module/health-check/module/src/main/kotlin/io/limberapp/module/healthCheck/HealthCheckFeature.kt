package io.limberapp.module.healthCheck

import io.limberapp.endpoint.healthCheck.HealthCheck
import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.service.healthCheck.HealthCheckService
import kotlin.reflect.KClass

class HealthCheckFeature(private val implClass: KClass<out HealthCheckService>) : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(HealthCheck::class)

  override fun bind() {
    bind(HealthCheckService::class.java).to(implClass.java).asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
