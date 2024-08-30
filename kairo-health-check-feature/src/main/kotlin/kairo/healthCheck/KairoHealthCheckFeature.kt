package kairo.healthCheck

import com.google.inject.PrivateBinder
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.toClass
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.bindRestHandlers
import kotlin.reflect.KClass

public class KairoHealthCheckFeature(
  private val healthCheckServiceClass: KClass<out HealthCheckService>,
) : Feature() {
  override val name: String = "Health Check"

  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bind<HealthCheckService>().toClass(healthCheckServiceClass)
    binder.bindRestHandlers<HealthCheckHandler>()
  }
}
