package limber.testing

import limber.client.gaeWarmup.GaeWarmupClient
import limber.config.ConfigImpl
import limber.feature.TestRestFeature
import limber.feature.googleAppEngine.GoogleAppEngineFeature
import limber.feature.healthCheck.HealthCheckFeature
import limber.service.healthCheck.TestHealthCheckService

private val config = ConfigImpl

private const val PORT: Int = 8081

internal abstract class IntegrationTest : FeatureIntegrationTest(
  config = config,
  featureUnderTest = GoogleAppEngineFeature(
    baseUrl = "http://localhost:$PORT",
  ),
  supportingFeatures = setOf(
    HealthCheckFeature(
      serviceKClass = TestHealthCheckService::class,
      baseUrl = "http://localhost:$PORT",
    ),
    TestRestFeature(port = PORT),
  ),
) {
  internal val gaeWarmupClient: GaeWarmupClient =
    injector.getInstance(GaeWarmupClient::class.java)
}
