package kairo.rest

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public abstract class BaseKairoRestFeature : Feature() {
  final override val name: String = "REST"

  final override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: Binder) {
    binder.bindRestHandlers {} // Supports Servers with no REST handlers.
    binder.bind<RestHandlerRegistry>().toProvider(RestHandlerRegistryProvider::class)
  }
}
