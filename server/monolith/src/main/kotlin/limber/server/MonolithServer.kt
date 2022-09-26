package limber.server

import limber.config.ConfigImpl
import limber.feature.HealthCheckFeature
import limber.feature.OrganizationFeature
import limber.rest.RestFeature
import limber.rest.RestImplementation
import limber.service.healthCheck.HealthCheckServiceImpl
import limber.sql.SqlFeature

internal class MonolithServer(config: ConfigImpl) : Server<ConfigImpl>(config) {
  override val features = setOf(
    HealthCheckFeature(HealthCheckServiceImpl::class, config.restClient.baseUrls.self),
    OrganizationFeature(RestImplementation.Local),
    RestFeature(config.rest),
    SqlFeature(config.sql),
  )
}
