package kairo.googleCloudSchedulerRest

import kairo.featureTesting.KairoServerTest
import kairo.googleCloudTasksTesting.TestGoogleCloudTasksFeature
import kairo.restTesting.TestKairoRestFeature
import kairo.serverTesting.TestServer

internal abstract class GoogleCloudSchedulerRestFeatureTest : KairoServerTest() {
  internal class Server : TestServer(
    featureUnderTest = KairoGoogleCloudSchedulerRestFeature(),
    supportingFeatures = setOf(
      TestGoogleCloudTasksFeature(),
      TestKairoRestFeature(),
    ),
  )

  override val server: Server = Server()
}
