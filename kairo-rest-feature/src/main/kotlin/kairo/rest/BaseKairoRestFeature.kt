package kairo.rest

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.exceptionHandler.ExceptionManager
import kairo.rest.handler.RestHandlerRegistry
import kairo.rest.handler.RestHandlerRegistryProvider
import kairo.rest.server.bindRestHandlers

public abstract class BaseKairoRestFeature : Feature() {
  final override val name: String = "REST"

  final override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bindRestHandlers {} // Supports Servers with no REST handlers.
    binder.bind<RestHandlerRegistry>().toProvider(RestHandlerRegistryProvider::class)
    binder.bind<ExceptionManager>().toInstance(ExceptionManager())
  }
}
