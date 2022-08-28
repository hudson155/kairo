package limber.testing

import com.google.inject.Injector
import io.mockk.clearAllMocks
import limber.config.TestConfig
import limber.feature.Feature
import limber.feature.TestFeature
import limber.feature.guid.DeterministicGuidGenerator
import limber.feature.guid.GuidGenerator
import limber.server.TestServer
import org.junit.jupiter.api.AfterEach

public abstract class ServerIntegrationTest(
  config: TestConfig,
  private val supportingFeatures: Set<Feature>,
  featureUnderTest: Feature,
) {
  private val server = TestServer(
    config = config,
    supportingFeatures = supportingFeatures,
    featureUnderTest = featureUnderTest,
  )

  protected val injector: Injector = checkNotNull(server.injector) {
    "The injector should not be null because TestServers start themselves automatically."
  }

  protected val guidGenerator: DeterministicGuidGenerator =
    injector.getInstance(GuidGenerator::class.java) as DeterministicGuidGenerator

  @AfterEach
  public fun afterEach() {
    supportingFeatures.filterIsInstance<TestFeature>().forEach { it.afterEach() }
    server.stop()
    clearAllMocks()
  }
}
