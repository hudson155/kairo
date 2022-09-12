package limber.server

import limber.config.ConfigImpl
import limber.feature.HealthCheckFeature
import limber.rest.RestFeature
import limber.service.healthCheck.HealthCheckServiceImpl

internal class MonolithServer(config: ConfigImpl) : Server<ConfigImpl>(config) {
  override val features = setOf(
    HealthCheckFeature(HealthCheckServiceImpl::class, config.restClient.baseUrls.self),
    RestFeature(config.rest),
  )
}
