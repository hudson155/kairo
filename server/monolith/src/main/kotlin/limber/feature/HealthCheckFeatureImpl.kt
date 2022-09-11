package limber.feature

import com.google.inject.PrivateBinder
import limber.client.HealthCheckClient
import limber.client.HttpHealthCheckClient
import limber.rest.bindClient
import limber.rest.bindHttpClient
import limber.service.HealthCheckService
import limber.service.healthCheck.HealthCheckServiceImpl

internal class HealthCheckFeatureImpl(private val baseUrl: String) : HealthCheckFeature() {
  override fun bind(binder: PrivateBinder) {
    super.bind(binder)

    binder.bind(HealthCheckService::class.java).to(HealthCheckServiceImpl::class.java)

    binder.bindHttpClient(baseUrl)
    binder.bindClient(HealthCheckClient::class, HttpHealthCheckClient::class)
  }
}
