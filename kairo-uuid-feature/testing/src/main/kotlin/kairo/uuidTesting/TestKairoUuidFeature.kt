package kairo.uuidTesting

import com.google.inject.Injector
import kairo.dependencyInjection.getInstance
import kairo.featureTesting.TestFeature
import kairo.uuid.DeterministicKairoUuidGenerator
import kairo.uuid.KairoUuidConfig
import kairo.uuid.KairoUuidFeature
import kairo.uuid.KairoUuidGenerator

/**
 * Extends [KairoUuidFeature] for testing by automatically resetting.
 */
public open class TestKairoUuidFeature(
  config: KairoUuidConfig,
) : KairoUuidFeature(config), TestFeature.BeforeEach {
  override suspend fun beforeEach(injector: Injector) {
    val uuidGenerator = injector.getInstance<KairoUuidGenerator>() as DeterministicKairoUuidGenerator
    uuidGenerator.reset()
  }
}
