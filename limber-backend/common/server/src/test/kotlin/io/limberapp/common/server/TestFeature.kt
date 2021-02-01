package io.limberapp.common.server

import io.limberapp.common.module.Feature
import io.limberapp.common.restInterface.EndpointHandler
import kotlin.reflect.KClass

internal object TestFeature : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      NoopGetHandler::class,
      EndpointWithoutAuthHandler::class,
      RequiresPermissionHandler::class,
      UnusualStatusCodeHandler::class,
      PathParamHandler::class,
      RequiredQpHandler::class,
      OptionalQpHandler::class,
      RequiredBodyHandler::class,
      OptionalBodyHandler::class,
  )

  override fun bind(): Unit = Unit

  override fun cleanUp(): Unit = Unit
}
