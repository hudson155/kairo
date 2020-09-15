package io.limberapp.common.module.healthCheck

import io.limberapp.common.endpoint.ApiEndpoint
import io.limberapp.common.module.Module
import io.limberapp.common.module.healthCheck.endpoint.healthCheck.HealthCheck
import kotlinx.serialization.modules.EmptySerializersModule

class HealthCheckModule : Module() {
  override val serializersModule = EmptySerializersModule

  override val endpoints = listOf<Class<out ApiEndpoint<*, *, *>>>(HealthCheck::class.java)

  override fun bindServices() = Unit
}
