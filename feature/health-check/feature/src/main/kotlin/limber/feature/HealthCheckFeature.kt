package limber.feature

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import limber.endpoint.GetHealthCheckLiveness
import limber.endpoint.GetHealthCheckReadiness
import limber.rep.HealthCheckRep
import limber.rest.bindRestEndpoint
import limber.service.HealthCheckService

public open class HealthCheckFeature : Feature() {
  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetHealthCheckLiveness::class)
    binder.bindRestEndpoint(GetHealthCheckReadiness::class)

    binder.expose(HealthCheckService::class.java)
  }

  final override fun afterStart(injector: Injector) {
    injector.getInstance(HealthCheckService::class.java).server =
      HealthCheckRep.State.Healthy
  }

  final override fun beforeStop(injector: Injector?) {
    injector ?: return
    injector.getInstance(HealthCheckService::class.java).server =
      HealthCheckRep.State.Unhealthy
  }
}
