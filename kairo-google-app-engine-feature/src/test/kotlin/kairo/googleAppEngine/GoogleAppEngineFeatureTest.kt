package kairo.googleAppEngine

import kairo.featureTesting.KairoServerTest
import kairo.healthCheck.KairoHealthCheckFeature
import kairo.restTesting.TestKairoRestFeature
import kairo.serverTesting.TestServer

internal abstract class GoogleAppEngineFeatureTest : KairoServerTest() {
  internal class Server : TestServer(
    featureUnderTest = KairoGoogleAppEngineFeature(),
    supportingFeatures = setOf(
      KairoHealthCheckFeature(TestHealthCheckService::class),
      TestKairoRestFeature(),
    ),
  )

  override val server: Server = Server()
}
