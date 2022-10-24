package limber.server

import limber.config.MonolithServerConfig
import limber.feature.healthCheck.HealthCheckFeature
import limber.feature.organization.OrganizationFeature
import limber.feature.rest.RestFeature
import limber.feature.rest.RestImplementation
import limber.feature.sql.SqlFeature
import limber.service.healthCheck.HealthCheckServiceImpl

internal class MonolithServer(config: MonolithServerConfig) : Server<MonolithServerConfig>(config) {
  override val features = setOf(
    HealthCheckFeature(HealthCheckServiceImpl::class, config.restClient.baseUrls.self),
    OrganizationFeature(RestImplementation.Local),
    RestFeature(config.rest),
    SqlFeature(config.sql),
  )
}
