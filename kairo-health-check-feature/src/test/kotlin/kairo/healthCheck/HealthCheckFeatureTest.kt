package kairo.healthCheck

import kairo.featureTesting.KairoServerTest
import kairo.restTesting.TestKairoRestFeature
import kairo.serverTesting.TestServer

internal abstract class HealthCheckFeatureTest : KairoServerTest() {
  internal class Server : TestServer(
    featureUnderTest = KairoHealthCheckFeature(TestHealthCheckService::class),
    supportingFeatures = setOf(
      TestKairoRestFeature(),
    ),
  )

  override val server: Server = Server()
}
