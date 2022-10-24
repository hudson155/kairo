package limber.testing

import limber.client.healthCheck.HealthCheckClient
import limber.config.ConfigImpl
import limber.feature.TestRestFeature
import limber.feature.healthCheck.HealthCheckFeature
import limber.service.healthCheck.TestHealthCheckService

private val config = ConfigImpl

private const val PORT: Int = 8081

internal abstract class IntegrationTest : FeatureIntegrationTest(
  config = config,
  featureUnderTest = HealthCheckFeature(
    serviceKClass = TestHealthCheckService::class,
    baseUrl = "http://localhost:$PORT",
  ),
  supportingFeatures = setOf(TestRestFeature(port = PORT)),
) {
  internal val healthCheckClient: HealthCheckClient =
    injector.getInstance(HealthCheckClient::class.java)
}
