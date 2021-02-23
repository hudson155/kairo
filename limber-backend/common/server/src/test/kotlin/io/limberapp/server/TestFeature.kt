package io.limberapp.server

import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import kotlin.reflect.KClass

internal object TestFeature : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      NoopGetHandler::class,
      EndpointWithoutAuthHandler::class,
      RequiresPermissionHandler::class,
      PathParamHandler::class,
      RequiredQpHandler::class,
      OptionalQpHandler::class,
      RequiredBodyHandler::class,
      OptionalBodyHandler::class,
  )

  override fun bind(): Unit = Unit

  override fun cleanUp(): Unit = Unit
}
