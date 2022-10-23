package limber.feature.healthCheck

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import limber.client.healthCheck.HealthCheckClient
import limber.client.healthCheck.HttpHealthCheckClient
import limber.endpoint.healthCheck.GetHealthCheckLiveness
import limber.endpoint.healthCheck.GetHealthCheckReadiness
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.bindClients
import limber.feature.rest.bindHttpClient
import limber.feature.rest.bindRestEndpoint
import limber.rep.healthCheck.HealthCheckRep
import limber.service.healthCheck.HealthCheckService
import kotlin.reflect.KClass

public class HealthCheckFeature(
  private val serviceKClass: KClass<out HealthCheckService>,
  private val baseUrl: String,
) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindHttpClient(baseUrl)
    bindHealthCheck(binder)
  }

  override fun afterStart(injector: Injector) {
    injector.getInstance(HealthCheckService::class.java).server =
      HealthCheckRep.State.Healthy
  }

  override fun beforeStop(injector: Injector?) {
    injector ?: return
    injector.getInstance(HealthCheckService::class.java).server =
      HealthCheckRep.State.Unhealthy
  }

  private fun bindHealthCheck(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetHealthCheckLiveness::class)
    binder.bindRestEndpoint(GetHealthCheckReadiness::class)

    // HealthCheckService must be a singleton because it stores state.
    binder.bind(HealthCheckService::class.java).to(serviceKClass.java).asEagerSingleton()
    binder.expose(HealthCheckService::class.java)

    binder.bindClients {
      bind(HealthCheckClient::class) { HttpHealthCheckClient::class }
    }
  }
}
