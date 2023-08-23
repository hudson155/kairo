package limber.server

import limber.config.MonolithServerConfig
import limber.feature.auth0.Auth0Feature
import limber.feature.event.EventFeature
import limber.feature.form.FormFeature
import limber.feature.googleAppEngine.GoogleAppEngineFeature
import limber.feature.healthCheck.HealthCheckFeature
import limber.feature.organization.OrganizationFeature
import limber.feature.rest.RestFeature
import limber.feature.rest.RestImplementation
import limber.feature.sql.SqlFeature
import limber.service.healthCheck.HealthCheckServiceImpl

internal class MonolithServer(config: MonolithServerConfig) : Server<MonolithServerConfig>(config) {
  override val features = setOf(
    Auth0Feature(config.auth0),
    EventFeature(config.event),
    FormFeature(RestImplementation.Local),
    GoogleAppEngineFeature(config.restClient.baseUrls.self),
    HealthCheckFeature(HealthCheckServiceImpl::class, config.restClient.baseUrls.self),
    OrganizationFeature(RestImplementation.Local),
    RestFeature(config.rest),
    SqlFeature(config.sql),
  )
}
