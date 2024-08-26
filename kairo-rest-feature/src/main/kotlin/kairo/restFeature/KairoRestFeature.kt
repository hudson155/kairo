package kairo.restFeature

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.expose
import kairo.dependencyInjection.getInstance
import kairo.feature.Feature
import kairo.feature.FeaturePriority

public class KairoRestFeature(
  private val config: KairoRestConfig,
) : Feature() {
  override val name: String = "REST"

  override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: PrivateBinder) {
    binder.bind<KairoRestConfig>().toInstance(config)

    binder.bind<RestServer>()
    binder.expose<RestServer>()
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    injector.getInstance<RestServer>().start()
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    injector.getInstance<RestServer>().stop()
  }
}
