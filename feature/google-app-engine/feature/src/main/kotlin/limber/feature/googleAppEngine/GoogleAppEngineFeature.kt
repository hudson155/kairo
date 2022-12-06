package limber.feature.googleAppEngine

import com.google.inject.PrivateBinder
import limber.client.gaeWarmup.GaeWarmupClient
import limber.client.gaeWarmup.HttpGaeWarmupClient
import limber.endpoint.gaeWarmup.GetGaeWarmup
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.bindClients
import limber.feature.rest.bindHttpClient
import limber.feature.rest.bindRestEndpoint

public class GoogleAppEngineFeature(private val baseUrl: String) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindHttpClient(baseUrl)
    bindGaeWarmup(binder)
  }

  private fun bindGaeWarmup(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetGaeWarmup::class)

    binder.bindClients {
      bind(GaeWarmupClient::class) { HttpGaeWarmupClient::class }
    }
  }
}
