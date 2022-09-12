package limber.testing

import com.google.inject.Injector
import limber.config.TestConfig
import limber.feature.Feature
import limber.feature.guid.DeterministicGuidGenerator
import limber.feature.guid.GuidGenerator
import limber.server.Server
import limber.server.TestServer

public abstract class FeatureIntegrationTest(
  config: TestConfig,
  featureUnderTest: Feature,
  supportingFeatures: Set<Feature> = emptySet(),
) : ServerIntegrationTest() {
  final override val server: Server<TestConfig> = TestServer(
    config = config,
    featureUnderTest = featureUnderTest,
    supportingFeatures = supportingFeatures,
  )

  protected val injector: Injector = checkNotNull(server.injector) {
    "The injector should not be null because TestServers start themselves automatically."
  }

  protected val guidGenerator: DeterministicGuidGenerator =
    injector.getInstance(GuidGenerator::class.java) as DeterministicGuidGenerator
}
