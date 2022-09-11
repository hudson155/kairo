package limber.server

import limber.config.ConfigImpl
import limber.feature.HealthCheckFeatureImpl
import limber.rest.RestFeature

internal class MonolithServer(config: ConfigImpl) : Server<ConfigImpl>(config) {
  override val features = setOf(
    HealthCheckFeatureImpl(config.restClient.baseUrls.self),
    RestFeature(config.rest),
  )
}
