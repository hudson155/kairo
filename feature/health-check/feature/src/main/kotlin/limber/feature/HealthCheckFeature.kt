package limber.feature

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import limber.client.HealthCheckClient
import limber.client.HttpHealthCheckClient
import limber.endpoint.GetHealthCheckLiveness
import limber.endpoint.GetHealthCheckReadiness
import limber.rep.HealthCheckRep
import limber.rest.bindClient
import limber.rest.bindHttpClient
import limber.rest.bindRestEndpoint
import limber.service.HealthCheckService
import kotlin.reflect.KClass

public class HealthCheckFeature(
  private val serviceKClass: KClass<out HealthCheckService>,
  private val baseUrl: String,
) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetHealthCheckLiveness::class)
    binder.bindRestEndpoint(GetHealthCheckReadiness::class)

    // HealthCheckService must be a singleton because it stores state.
    binder.bind(HealthCheckService::class.java).to(serviceKClass.java).asEagerSingleton()
    binder.expose(HealthCheckService::class.java)

    binder.bindHttpClient(baseUrl)
    binder.bindClient(HealthCheckClient::class, HttpHealthCheckClient::class)
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
}
