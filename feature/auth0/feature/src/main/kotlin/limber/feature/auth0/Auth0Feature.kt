package limber.feature.auth0

import com.google.inject.PrivateBinder
import limber.config.auth0.Auth0Config
import limber.feature.Feature
import limber.feature.FeaturePriority

public open class Auth0Feature(private val config: Auth0Config) : Feature() {
  final override val priority: FeaturePriority = FeaturePriority.Normal

  final override fun bind(binder: PrivateBinder) {
    bindConfig()
    bindManagementApi()
  }

  private fun bindConfig() {
    if (config.managementApi is Auth0Config.ManagementApi.Real) {
      bind(Auth0Config.ManagementApi.Real::class.java).toInstance(config.managementApi)
    }
  }

  private fun bindManagementApi() {
    val managementApiKclass = when (config.managementApi) {
      is Auth0Config.ManagementApi.Fake -> FakeAuth0ManagementApi::class
      is Auth0Config.ManagementApi.Real -> RealAuth0ManagementApi::class
    }
    bind(Auth0ManagementApi::class.java).to(managementApiKclass.java).asEagerSingleton()
    expose(Auth0ManagementApi::class.java)
  }
}
